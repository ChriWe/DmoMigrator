package com.cw.migrator;

import com.cw.database.JDBCConfig;
import com.cw.database.PostGISJDBC;
import com.cw.threads.ThreadFactory;
import com.cw.utils.OSConfig;
import com.cw.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by Christoph on 18.11.2015.
 */
public class Watcher {

    private OSConfig osConfig;
    private ArrayList<String> dmoList;
    private Thread dmoThread;
    private ThreadFactory threadFactory;
    private WatchService watcher;
    private WatchKey key;

    public Watcher(OSConfig osConfig) throws IOException {
        this.osConfig = osConfig;
        this.dmoList = Utils.getDmoFiles();
        this.threadFactory = new ThreadFactory();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.key = osConfig.getTrjDir().toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        this.dmoThread = threadFactory.initDmoThread(this.dmoList.get(0));
    }

    public void watch() {
        int dmoCount = 0;

        while (true) {
            WatchKey key;
            try {
                // wait for a key to be available
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
//                System.out.println(kind.name() + ": " + fileName);

                if (kind == OVERFLOW) {
                    continue;
                } else if (kind == ENTRY_CREATE) {

                } else if (kind == ENTRY_DELETE) {

                } else if (kind == ENTRY_MODIFY) {

                    if (fileName.toString().equals(osConfig.getTrjDefaultName())) {

                        if (dmoCount < dmoList.size()) {
                            String dmoName = dmoList.get(dmoCount);
                            String csvName = dmoName + ".csv";
                            Utils.renameTrj(fileName.toString(), csvName);
                            System.out.println("-- " + (dmoCount + 1) + ". Creation: " + csvName);

                            if (dmoCount < dmoList.size() - 1) {
                                String nextDmo = dmoList.get(dmoCount + 1);
                                this.dmoThread = threadFactory.initDmoThread(nextDmo);
                            }

                            dmoCount++;
                        }

                        if (dmoCount == dmoList.size()) {
                           initPostGISMigration();
                        }
                    }
                }
            }

            // IMPORTANT: The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void initPostGISMigration() {
        System.out.println("-- Done converting all dmo´s to csv´s.");
        System.out.println("-- Starting PostGIS migration.");

        JDBCConfig jdbcConfig = new JDBCConfig();
        PostGISJDBC postGISJDBC = new PostGISJDBC(jdbcConfig);
        postGISJDBC.init();

        File[] trjHome = new File(osConfig.getTrjHomePath()).listFiles();
        for (File trjFile: trjHome) {
            TrjParser trjParser = new TrjParser(trjFile);
            trjParser.writeTrjToPostGIS(jdbcConfig, postGISJDBC);
            System.out.println("-- Done migrating csv " + trjFile.getName());
        }

        postGISJDBC.close();

        System.out.println("-- Done migrating all csv´s.");
        System.out.println("-- Exiting DMO - Migrator.");
        System.exit(0);
    }
}
