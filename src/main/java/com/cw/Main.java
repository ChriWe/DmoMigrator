package com.cw;

import com.cw.threads.ThreadManager;
import com.cw.utils.SbConfig;
import com.cw.utils.Utils;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws AWTException, IOException {
        System.out.println("|| Starting DMO - Migrator ||");

        SbConfig sbConfig = new SbConfig();
        sbConfig.printConfig();

        if (Utils.checkRequirements()) {
            new ThreadManager.Maker().make(sbConfig).start();
        }
    }

}
