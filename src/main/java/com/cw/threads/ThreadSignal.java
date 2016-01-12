package com.cw.threads;

/**
 * Created by Christoph on 29.12.2015.
 */
public class ThreadSignal {

    private boolean dmoDone = false;
    private String dmoName;

    // Singleton Implementation
    private ThreadSignal() {}

    private static class SingletonHelper {
        private static final ThreadSignal INSTANCE = new ThreadSignal();
    }

    public static ThreadSignal getInstance() {
        return SingletonHelper.INSTANCE;
    }


    public boolean isDmoDone(){
        return this.dmoDone;
    }

    public synchronized void setDmoDone(boolean dmoDone){
        this.dmoDone = dmoDone;

        if (dmoDone && Thread.currentThread().getName().equals(MigratorThreads.WATCH.toString())) {
//            System.out.println(Thread.currentThread().getName() + " notifies");
            notify();
        }
        if (!dmoDone && Thread.currentThread().getName().equals(MigratorThreads.DMO.toString())) {
            try {
//                System.out.println(Thread.currentThread().getName() + " waits");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public String getDmoName() {
        return dmoName;
    }

    public synchronized void setDmoName(String dmoName) {
        this.dmoName = dmoName;
    }
}
