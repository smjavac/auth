package ru.said;

<<<<<<< HEAD
import com.sun.xml.internal.ws.util.StringUtils;
=======
import org.apache.commons.lang3.StringUtils;
>>>>>>> 00a8c5d038b6913bdb964fca3a8c2332b367f151
import org.apache.log4j.Logger;
import java.lang.Object;
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
<<<<<<< HEAD
//     private boolean pathTrue(String path) throws IOException {
//        if (System.getProperty(path) != null) {
//            return true;
//        }
//        throw new
//     }
=======


>>>>>>> 00a8c5d038b6913bdb964fca3a8c2332b367f151
}
