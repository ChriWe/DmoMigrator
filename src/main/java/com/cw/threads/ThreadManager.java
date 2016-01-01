package com.cw.threads;

import com.cw.utils.SbConfig;

/**
 * Created by Christoph on 28.12.2015.
 */
public class ThreadManager {

    private final MigratorThreadFactory migratorThreadFactory;

    private ThreadManager(SbConfig sbConfig) {
        this.migratorThreadFactory = new MigratorThreadFactory.Maker().make(sbConfig, new ThreadSignal());
    }

    public void start() {
        this.migratorThreadFactory.newThread(MigratorThreads.SB).start();
        this.migratorThreadFactory.newThread(MigratorThreads.WATCH).start();
        this.migratorThreadFactory.newThread(MigratorThreads.DMO).start();
    }

    public static class Maker {

        public ThreadManager make(SbConfig sbConfig) {
            return new ThreadManager(sbConfig);
        }

    }
}
