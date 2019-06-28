package ru.said;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.util.Properties;

public class DatabaseUtils {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class);
    private static String JDBC_DRIVER;
    private static String DATABASE_URL;
    private static String USER;
    private static String PASSWORD;


    static {
        Properties property = new Properties();
        String key = "confi.properties";
        if (StringUtils.isBlank(System.getProperty(key))) {
            try {
                throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                LOGGER.error("ОШИБКА: неверный ключ \"config.properties\"", e);
                //   System.exit(202);
            }
        }

        if (!new File(System.getProperty(key)).exists()) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                LOGGER.error("ОШИБКА: файл \"config.properties\" не существует", e);
            }
        } else {

            try (FileInputStream fis = new FileInputStream(System.getProperty("config.properties"))
            ) {
                property.load(fis);
                JDBC_DRIVER = property.getProperty("jdbc.driver");
                DATABASE_URL = property.getProperty("db.host");
                USER = property.getProperty("db.login");
                PASSWORD = property.getProperty("db.password");
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private DatabaseUtils() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return connection;
    }


}
