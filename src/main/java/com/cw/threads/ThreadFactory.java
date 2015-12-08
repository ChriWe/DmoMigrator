package com.cw.threads;

import com.cw.migrator.DmoRobot;
import com.cw.migrator.Watcher;
import com.cw.utils.OSConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Christoph on 01.12.2015.
 */
public class ThreadFactory {

    private OSConfig OSConfig;

    public Thread initWatchThread(OSConfig OSConfig) {
        this.OSConfig = OSConfig;
        watchThread().start();

        return null;
    }
    public Thread initSbThread(OSConfig OSConfig) {
        this.OSConfig = OSConfig;
        sbThread().start();

        return null;
    }
    public Thread initDmoThread(String dmo) {
        dmoThread(dmo).start();
        return null;
    }

    private Thread watchThread() {
        Thread watchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("watch");
                Watcher watcher = null;
                try {
                    watcher = new Watcher(OSConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                watcher.watch();
            }
        });

        return watchThread;
    }

    private Thread dmoThread(final String dmo) {
        Thread dmoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("dmo");
                try {
                    DmoRobot dmoRobot = new DmoRobot();
                    dmoRobot.executeRobot(dmo);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });

        return dmoThread;
    }

    private Thread sbThread() {
        Thread sbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("sb");
                Process sb = runSb(OSConfig.getSbHome());
            }
        });

        return sbThread;
    }

    private Process runSb(String sbPath) {
        File sbDir = new File(sbPath);
        java.util.List<String> exec = null;

        if (OSConfig.getOS().equals("WIN64")) {
            exec = Arrays.asList("\"" + sbPath + "/bin64/sauerbraten.exe\"", "-t0", "-w640", "-h480", "-qhome");
        } else if (OSConfig.getOS().equals("LINUX")) {
            exec = Arrays.asList("/bin/sh", "/home/juergen/Downloads/sauerbraten/sauerbraten_unix");
        }

        ProcessBuilder pb = new ProcessBuilder(exec);
        pb.directory(sbDir);
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

}
