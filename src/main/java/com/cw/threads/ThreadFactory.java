package com.cw.threads;

import com.cw.migrator.DmoRobot;
import com.cw.migrator.Watcher;
import com.cw.utils.Config;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Christoph on 01.12.2015.
 */
public class ThreadFactory {

    private Config config;

    public Thread initWatchThread(Config config) {
        this.config = config;
        watchThread().start();

        return null;
    }
    public Thread initSbThread(Config config) {
        this.config = config;
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
                Watcher watcher = null;
                try {
                    watcher = new Watcher(config);
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
                Process sb = runSb(config.getSbHome());
            }
        });

        return sbThread;
    }

    private Process runSb(String sbPath) {
        File sbDir = new File(sbPath);
        java.util.List<String> exec = null;

        if (config.getOS().equals("WIN64")) {
            exec = Arrays.asList("\"" + sbPath + "/bin64/sauerbraten.exe\"", "-t0", "-w640", "-h480", "-qhome");
        } else if (config.getOS().equals("LINUX")) {
            exec = Arrays.asList("/bin/sh","/home/juergen/Downloads/sauerbraten/sauerbraten_unix");
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
