package com.example.app.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String JDBC_URL = "jdbc:mysql://sql12.freesqldatabase.com/sql12673047";
    private static final String USERNAME = "sql12673047";
    private static final String PASSWORD = "GSqLagGEdg";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
