package ru.said;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.util.Properties;

public class Util {
    private static String JDBC_DRIVER;
    private static String DATABASE_URL;
    private static  String USER;
    private static  String PASSWORD;
    private static FileInputStream fis;
    private static Properties property;
    private Util() {

    }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            fis = new FileInputStream("F:\\java\\IDEA\\My projects\\AUTH\\src\\main\\resources\\config.properties");
        }        catch (FileNotFoundException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        try {
            property = new Properties();
            property.load(fis);
        }        catch (IOException e) {
            System.out.println(e);
        }
        try {
            JDBC_DRIVER = property.getProperty("jdbc.driver");
            DATABASE_URL = property.getProperty("db.host");
            USER = property.getProperty("db.login");
            PASSWORD = property.getProperty("db.password");
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return connection;
    }
}
