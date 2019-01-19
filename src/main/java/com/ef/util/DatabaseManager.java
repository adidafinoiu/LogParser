package com.ef.util;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://192.168.99.100:3306/log";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "pass";

    private Connection con;

    public Connection getDbConnection() {
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            sqle.printStackTrace();
        }
        return con;
    }

}