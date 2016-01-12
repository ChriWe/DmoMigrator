package com.cw.migrator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Christoph on 02.12.2015.
 */
public class MigratorConfig {

    private static final String TRJ_DEFAULT_NAME = "trajectorie.csv";
    private static final String RECORD_END_FILE = "record_end_file.csv";

    private final String SB_HOME;
    private final String TRJ_HOME;
    private final String DMO_HOME;
    private final String SB_EXEC;
    private final String SB_START_PARAMS;
    private final int ROBOT_START_DELAY;
    private final int ROBOT_GAMESPEED_DELAY;

    public MigratorConfig() {
        Properties config = getConfig();
        this.SB_HOME = config.getProperty("SB_HOME");
        this.TRJ_HOME = config.getProperty("TRJ_HOME");
        this.DMO_HOME = config.getProperty("DMO_HOME");
        this.SB_EXEC = config.getProperty("SB_EXEC");
        this.SB_START_PARAMS = config.getProperty("SB_START_PARAMS");
        this.ROBOT_START_DELAY = Integer.parseInt(config.getProperty("ROBOT_START_DELAY"));
        this.ROBOT_GAMESPEED_DELAY = Integer.parseInt(config.getProperty("ROBOT_GAMESPEED_DELAY"));
    }

    public static Properties getConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            input = classloader.getResourceAsStream("properties/sb.properties");
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

    public String getSbDirPath() {
        return SB_HOME;
    }

    public String getTrjHomePath() {
        return TRJ_HOME;
    }

    public String getTrjDefaultName() {
        return TRJ_DEFAULT_NAME;
    }

    public String getTrjFilePath() {
        return TRJ_HOME + "/" + TRJ_DEFAULT_NAME;
    }

    public String getDmoDirPath() {
        return DMO_HOME;
    }

    public String getRecordEndFileName() {
        return RECORD_END_FILE;
    }

    public String getRecordEndFilePath() { return TRJ_HOME + "/" + RECORD_END_FILE; }

    public String getSbExecDir() {
        return SB_EXEC;
    }

    public String getSbStartParams() {
        return SB_START_PARAMS;
    }

    public int getRobotStartDelay() { return ROBOT_START_DELAY; }

    public int getRobotGamespeedDelay() { return ROBOT_GAMESPEED_DELAY; }

    public void printConfig() {
        System.out.println("-- Loading config:"
                + "\n--- SB_HOME: " + this.SB_HOME
                + "\n--- TRJ_HOME: " + this.TRJ_HOME
                + "\n--- DMO_HOME: " + this.DMO_HOME
                + "\n--- SB_START_PARAMS: " + this.SB_START_PARAMS
                + "\n--- SB_EXEC: " + this.SB_EXEC);
    }
}
