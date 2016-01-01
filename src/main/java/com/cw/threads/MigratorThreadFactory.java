package com.cw.threads;

import com.cw.database.JDBCConfig;
import com.cw.database.PostGISJDBC;
import com.cw.migrator.DmoRobot;
import com.cw.migrator.TrjParser;
import com.cw.migrator.Watcher;
import com.cw.utils.SbConfig;
import com.cw.utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Christoph on 01.12.2015.
 */
public class MigratorThreadFactory {

    private final SbConfig sbConfig;
    private ThreadSignal threadSignal;

    private MigratorThreadFactory(SbConfig sbConfig, ThreadSignal threadSignal) {
        this.sbConfig = sbConfig;
        this.threadSignal = threadSignal;
    }

    public Thread newThread(MigratorThreads migratorThread) {
        switch (migratorThread) {
            case WATCH:
                return watchThread();
            case SB:
                return sbThread();
            case DMO:
                return dmoThread();
        }
        return null;
    }

    private Thread watchThread() {

        Thread watchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Watcher watcher = new Watcher(sbConfig);
                    watcher.watch(threadSignal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        watchThread.setName(MigratorThreads.WATCH.toString());

        return watchThread;
    }

    private Thread dmoThread() {

        Thread dmoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dmoList = Utils.getDmoFiles();
                int dmoCount = 0;
                for (String dmoName : dmoList) {

                    boolean execution = new DmoRobot.Maker().make(sbConfig).execute(dmoName);
                    if (execution) {
                        threadSignal.setDmoName(dmoName);
                        threadSignal.setDmoDone(false);
                        dmoCount++;
                    }

                    String csvName = dmoName + ".csv";

                    boolean renamed = Utils.renameTrj(sbConfig.getTrjDefaultName(), csvName);
                    if (!renamed) {
                        System.out.println("-- Renaming for " + sbConfig.getTrjDefaultName() + " to "
                                + csvName + " failed.");
                    } else {
                        System.out.println("-- " + dmoCount + ". Creation: " + csvName + " done.");
                    }

                    boolean deleted = new File(sbConfig.getRecordEndFilePath()).delete();
                    if (!deleted) {
                        System.out.println("-- Deletion for " + sbConfig.getRecordEndFileName() + " failed.");
                    }

                }

                System.out.println("-- Done converting all dmo´s to csv´s.");
                initPostGISMigration();
                System.out.println("-- Exiting DMO - Migrator.");
                System.exit(0);
            }
        });

        dmoThread.setName(MigratorThreads.DMO.toString());

        return dmoThread;
    }

    private Thread sbThread() {

        Thread sbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runSb();
            }
        });

        sbThread.setName(MigratorThreads.SB.toString());

        return sbThread;
    }

    private Process runSb() {
        ArrayList<String> exec = new ArrayList<>();
        exec.add(sbConfig.getSbExecDir());
        exec.addAll(Arrays.asList(sbConfig.getSbStartParams().split(" ")));
        File executionDir = new File(sbConfig.getSbDirPath());

        ProcessBuilder pb = new ProcessBuilder(exec);
        pb.directory(executionDir);
        pb.redirectErrorStream(true); // merge stdout, stderr of process

        Process p = null;
        try {
            System.out.println("-- Initialize Sauerbraten");
            p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }

    private void initPostGISMigration() {
        System.out.println("-- Starting PostGIS migration.");

        JDBCConfig jdbcConfig = new JDBCConfig();
        PostGISJDBC postGISJDBC = new PostGISJDBC(jdbcConfig);
        postGISJDBC.init();

        File[] trjHome = new File(sbConfig.getTrjHomePath()).listFiles();
        int migrationCount = 0;
        for (File trjFile : trjHome) {
            TrjParser trjParser = new TrjParser(trjFile);
            trjParser.writeTrjToPostGIS(jdbcConfig, postGISJDBC);
            migrationCount++;
            System.out.println("-- " + migrationCount + ". Migration: " + trjFile.getName() + " done.");
        }

        postGISJDBC.close();
        System.out.println("-- Done migrating all files.");
    }

    public static class Maker {

        public MigratorThreadFactory make(SbConfig sbConfig, ThreadSignal threadSignal) {
            return new MigratorThreadFactory(sbConfig, threadSignal);
        }

    }

}
