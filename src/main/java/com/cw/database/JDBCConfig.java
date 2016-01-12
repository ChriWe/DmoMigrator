package com.cw.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Christoph on 07.12.2015.
 */
public class JDBCConfig {

    private final String host;
    private final String port;
    private final String db;
    private final String schema;
    private final String user;
    private final String password;

    public JDBCConfig() {
        Properties config = getConfig();
        this.host = config.getProperty("HOST");
        this.port = config.getProperty("PORT");
        this.db = config.getProperty("DATABASE");
        this.schema = config.getProperty("SCHEMA");
        this.user = config.getProperty("USER");
        this.password = config.getProperty("PASSWORD");
    }

    private Properties getConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            input = classloader.getResourceAsStream("proeprties/jdbc.properties");
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


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDb() {
        return db;
    }

    public String getSchema() {
        return schema;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
