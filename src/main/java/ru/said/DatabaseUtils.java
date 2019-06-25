package ru.said;

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
        try (FileInputStream fis = new FileInputStream(System.getProperty("config.properties"))
        ) {
            property = new Properties();
            property.load(fis);
        } catch (FileNotFoundException e) {
            LOGGER.error("ОШИБКА: проверь путь до файла config.properties", e);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }catch (ExceptionInInitializerError e){
            LOGGER.error("ОШИБКА: файл не существует");
        }
        JDBC_DRIVER = property.getProperty("jdbc.driver");
        DATABASE_URL = property.getProperty("db.host");
        USER = property.getProperty("db.login");
        PASSWORD = property.getProperty("db.password");
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
