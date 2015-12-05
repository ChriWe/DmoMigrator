package com.cw.migrator;

import com.cw.threads.ThreadFactory;
import com.cw.utils.Config;
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

    private String TRJ_NAME = "trajectorie.csv";
    private Config config;
    private File trjDir;
    private ArrayList<String> dmoList;
    private Thread dmoThread;
    private ThreadFactory threadFactory;
    private WatchService watcher;
    private WatchKey key;

    public Watcher(Config config) throws IOException {

        this.config = config;
        this.trjDir = new File(config.getSbHome() + "/trajectories");
        this.dmoList = Utils.getDmoFiles(config.getSbHome());
        this.threadFactory = new ThreadFactory();
        this.watcher = FileSystems.getDefault().newWatchService();
        this.key = trjDir.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);


        this.dmoThread = threadFactory.initDmoThread(this.dmoList.get(0));
//        cleanupTrj();
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
                // get event type
                WatchEvent.Kind<?> kind = event.kind();

                // get file name
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();

                System.out.println(kind.name() + ": " + fileName);

                if (kind == OVERFLOW) {
                    continue;
                } else if (kind == ENTRY_CREATE) {
                    //TODO: take CSV and migrate to PostGIS
                } else if (kind == ENTRY_DELETE) {

                } else if (kind == ENTRY_MODIFY) {

                    // just modify trajectories
                    if (fileName.toString().equals(TRJ_NAME)) {
                        // just start new Thread if a trajectorie is ready
                        if (dmoCount < dmoList.size()) {
                            String dmoName = dmoList.get(dmoCount);
                            String csvName = dmoName + ".csv";
                            renameTrj(fileName.toString(), csvName);
                            System.out.println("-- " + (dmoCount + 1) + ". Creation: " + csvName);

                            if (dmoCount < dmoList.size() - 1) {
                                String nextDmo = dmoList.get(dmoCount + 1);
                                this.dmoThread = threadFactory.initDmoThread(nextDmo);
                            }


                            dmoCount++;
                        }


                        if (dmoCount == dmoList.size()) {
                            System.out.println("-- Done converting all dmo´s. Exiting DMO - Migrator");
                            System.exit(0);
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

    private void cleanupTrj() {
        File[] trjFiles = trjDir.listFiles();
        for (File trjFile : trjFiles) {
            if (trjFile.getName().equals(TRJ_NAME)) {
                trjFile.delete();
                System.out.println("-- Cleaned trajectories.csv");
            }
        }
    }

    private void renameTrj(String fileName, String newName) {
        File trjFile = new File(trjDir.toString() + "/" + fileName);
        File newFile = new File(trjDir.toString() + "/" + newName);

        if (newFile.exists()) {
            System.out.println("-- Overriding file " + newName);
            newFile.delete();
        }

        trjFile.renameTo(newFile);
    }

}
