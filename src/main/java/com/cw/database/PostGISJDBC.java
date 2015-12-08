package com.cw.database;

import com.cw.database.entries.GameEntry;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

/**
 * Created by Christoph on 05.12.2015.
 */
public class PostGISJDBC {
    private Connection c = null;
    private final JDBCConfig jdbcConfig;

    public PostGISJDBC(JDBCConfig JDBCConfig) {
        this.jdbcConfig = JDBCConfig;
    }

    public void init() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://"
                            + this.jdbcConfig.getHost() + ":"
                            + this.jdbcConfig.getPort() + "/"
                            + this.jdbcConfig.getDb(),
                    this.jdbcConfig.getUser(),
                    this.jdbcConfig.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("-- Opened database connection successfully");
    }

    public void close() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("-- Closed database connection successfully");
    }

    public void execute(String sql) {
        try {
            Statement stmt = c.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executePreparedStatement(String preparedSQL, List preparedObjects) throws MissingResourceException {
        try {
            PreparedStatement preparedStatement = c.prepareStatement(preparedSQL);

            for (int i = 1; i <= preparedObjects.size(); i++) {
                Object object = preparedObjects.get(i - 1);
                preparedStatement.setObject(i, object, Types.OTHER);
            }

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
