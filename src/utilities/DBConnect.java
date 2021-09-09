package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private static String HOST = "localhost";
    private static int PORT = 3306;
    private static String DB_NAME = "school";
    private static String USERNAME = "root";
    private static String PASSWORD = "password";
    private static Connection connection;

    public static Connection getConnection() {
        String connectionUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME);
        try {
            connection = DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD);
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        return connection;
    }
}
