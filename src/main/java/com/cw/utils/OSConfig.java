package com.cw.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Christoph on 02.12.2015.
 */
public class OSConfig {

    private String OS;
    private String SB_HOME;
    private String TRJ_HOME;
    private String DMO_HOME;
    private String TRJ_DEFAULT_NAME = "trajectorie.csv";

    public OSConfig() {
        Properties config = getConfig();
        this.OS = config.getProperty("OS");
        this.SB_HOME = config.getProperty("SB_HOME");
        this.TRJ_HOME = config.getProperty("TRJ_HOME");
        this.DMO_HOME = config.getProperty("DMO_HOME");
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


    public String getOs() {
        return OS;
    }

    public String getSbDirPath() {
        return SB_HOME;
    }

    public String getTrjHomePath() {
        return TRJ_HOME;
    }

    public File getTrjDir() {
        return new File(TRJ_HOME);
    }

    public String getDmoDirPath() {
        return DMO_HOME;
    }

    public File getDmoDir() {
        return new File(DMO_HOME);
    }

    public String getTrjDefaultName() {
        return TRJ_DEFAULT_NAME;
    }

    public void printConfig() {
        System.out.println("-- Loading config:\n--- OS: " + this.OS
                + "\n--- SB_HOME: " + this.SB_HOME
                + "\n--- TRJ_HOME: " + this.TRJ_HOME
                + "\n--- DMO_HOME: " + this.DMO_HOME);
    }
}
