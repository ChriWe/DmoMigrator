package com.cw.migrator;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by Christoph on 18.11.2015.
 */
public class Watcher {

    private String trajectoriesPath;
    private File trajectoriesDir;
    private Path dir;
    private WatchService watcher;
    private WatchKey key;

    public Watcher() throws IOException {
        trajectoriesPath = System.getenv("SB_HOME") + "/trajectories";
        trajectoriesDir = new File(trajectoriesPath);
        dir = trajectoriesDir.toPath();
        watcher = FileSystems.getDefault().newWatchService();
        key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void watch() {
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
                    System.out.println("trajectory created");
                } else if (kind == ENTRY_DELETE) {
                    System.out.println("trajectory delted");
                } else if (kind == ENTRY_MODIFY) {
                    System.out.println("trajectory modified");
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
