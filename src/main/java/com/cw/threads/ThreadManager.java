package com.cw.threads;

import com.cw.migrator.MigratorConfig;

/**
 * Created by Christoph on 28.12.2015.
 */
public class ThreadManager {

    private final MigratorThreadFactory migratorThreadFactory;

    private ThreadManager(MigratorConfig migratorConfig) {
        this.migratorThreadFactory = new MigratorThreadFactory.Maker().make(migratorConfig, ThreadSignal.getInstance());
    }

    public void start() {
        this.migratorThreadFactory.newThread(MigratorThreads.DMO).start();
        this.migratorThreadFactory.newThread(MigratorThreads.WATCH).start();
    }

    public static class Maker {

        public ThreadManager make(MigratorConfig migratorConfig) {
            return new ThreadManager(migratorConfig);
        }

    }
}
