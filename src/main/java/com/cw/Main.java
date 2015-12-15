package com.cw;

import com.cw.threads.ThreadFactory;
import com.cw.utils.OSConfig;
import com.cw.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws AWTException, IOException {
        System.out.println("|| Starting DMO - Migrator ||");

        OSConfig osConfig = new OSConfig();
        osConfig.printConfig();

        if (Utils.checkRequirements()) {
            ThreadFactory threadFactory = new ThreadFactory();
            threadFactory.initWatchThread(osConfig);
            threadFactory.initSbThread(osConfig);
        }
    }

}
