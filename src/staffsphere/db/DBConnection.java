package staffsphere.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class DBConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        // First try environment variables (recommended for GitHub deployments)
        URL = System.getenv("DB_URL");
        USER = System.getenv("DB_USER");
        PASSWORD = System.getenv("DB_PASSWORD");

        // If any value is missing, try to load from a config file named 'config.properties'
        if (URL == null || USER == null || PASSWORD == null) {
            Properties props = new Properties();
            try (InputStream in = new FileInputStream("config.properties")) {
                props.load(in);
                if (URL == null) URL = props.getProperty("db.url");
                if (USER == null) USER = props.getProperty("db.user");
                if (PASSWORD == null) PASSWORD = props.getProperty("db.password");
            } catch (Exception e) {
                // Missing config file is okay — we'll handle nulls at runtime.
            }
        }
    }

    public static Connection getConnection() {
        if (URL == null || USER == null || PASSWORD == null) {
            System.err.println("Database configuration not found.\n" +
                    "Please set environment variables DB_URL, DB_USER, DB_PASSWORD\n" +
                    "or create a config.properties file with db.url, db.user, db.password.");
            return null;
        }

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
