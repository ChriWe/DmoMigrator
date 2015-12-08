package com.cw;

import com.cw.database.JDBCConfig;
import com.cw.database.PostGISJDBC;
import com.cw.migrator.TrjParser;
import com.cw.threads.ThreadFactory;
import com.cw.utils.OSConfig;
import com.cw.utils.Utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws AWTException, IOException {
        System.out.println("|| Starting DMO - Migrator ||");

        OSConfig osConfig = new OSConfig();

        if (Utils.checkRequirements()) {
            ThreadFactory threadFactory = new ThreadFactory();
            threadFactory.initWatchThread(osConfig);
            threadFactory.initSbThread(osConfig);
        }
    }

}
