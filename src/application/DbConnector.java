package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
        private static final String USERNAME = "rootLab44889";
        private static final String PASSWORD = "rootLab44889!";
        private static final String CONN_STRING = "jdbc:mysql://50.62.209.41:3306/Lab4";

        // connection  method that connects us to the MySQL database
        public static Connection getConnection() throws SQLException {
            //System.out.println("Connected to student database successfully!");
            return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        }
    public static void displayException(SQLException ex) {

        System.err.println("Error Message: " + ex.getMessage());
        System.err.println("Error Code: " + ex.getErrorCode());
        System.err.println("SQL Status: " + ex.getSQLState());

    }
}
