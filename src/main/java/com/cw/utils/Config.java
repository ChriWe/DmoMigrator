package com.cw.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Christoph on 02.12.2015.
 */
public class Config {

    private String OS;
    private String SB_Home;

    public Config() {
        HashMap<String, String> config = getConfig();
        this.OS = config.get("OS");
        this.SB_Home = config.get("SB_HOME");
        System.out.println("-- Loading config:\n- OS: " + this.OS + "\n- SB_HOME" + this.SB_Home);
    }

    public static HashMap getConfig() {
        HashMap<String, String> config = new HashMap<>();
        Properties prop = new Properties();
        InputStream input = null;

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            input = classloader.getResourceAsStream("os.properties");
            prop.load(input);
            config.put("OS", prop.getProperty("OS"));
            config.put("SB_HOME", prop.getProperty("SB_HOME"));
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

        return config;
    }


    public String getOS() {
        return OS;
    }

    public String getSbHome() {
        return SB_Home;
    }

}
