package com.cw.migrator;

import com.cw.threads.ThreadSignal;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by Christoph on 18.11.2015.
 */
public class Watcher {

    private final MigratorConfig migratorConfig;
    private WatchService watcher;
    private WatchKey key;

    public Watcher(MigratorConfig migratorConfig) throws IOException {
        this.migratorConfig = migratorConfig;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.key = new File(migratorConfig.getTrjHomePath()).toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void watch(ThreadSignal threadSignal) {
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

                    if (fileName.toString().equals(migratorConfig.getRecordEndFileName())) {
                        threadSignal.setDmoDone(true);
                    }

                } else if (kind == ENTRY_DELETE) {

                } else if (kind == ENTRY_MODIFY) {

                }
            }

            // IMPORTANT: The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

}
