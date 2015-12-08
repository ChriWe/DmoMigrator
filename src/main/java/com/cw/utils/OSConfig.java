package com.cw.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Christoph on 02.12.2015.
 */
public class OSConfig {

    private String OS;
    private String SB_HOME;
    private String TRJ_DEFAULT_NAME = "trajectorie.csv";

    public OSConfig() {
        Properties config = getConfig();
        this.OS = config.getProperty("OS");
        this.SB_HOME = config.getProperty("SB_HOME");
        System.out.println("-- Loading config:\n--- OS: " + this.OS + "\n--- SB_HOME: " + this.SB_HOME);
    }

    public static Properties getConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            input = classloader.getResourceAsStream("os.properties");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }


    public String getOS() {
        return OS;
    }

    public String getSbHome() {
        return SB_HOME;
    }

    public String getTrjHome() {
        return SB_HOME + "/trajectories";
    }

    public File getTrjFile() {
        return new File(SB_HOME + "/trajectories");
    }

    public String getDmoHome() {
        return SB_HOME + "/dmo";
    }

    public File getDmoFile() {
        return new File(SB_HOME + "/dmo");
    }

    public String getTrjName() {
        return TRJ_DEFAULT_NAME;
    }
}
