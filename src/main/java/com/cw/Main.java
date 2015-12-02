package com.cw;

import com.cw.threads.ThreadFactory;
import com.cw.utils.Config;

import java.awt.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws AWTException, IOException {
        System.out.println("|| Starting DMO - Migrator ||");

        Config config = new Config();
        ThreadFactory threadFactory = new ThreadFactory();
        threadFactory.initWatchThread(config);
        threadFactory.initSbThread(config);
    }

}
