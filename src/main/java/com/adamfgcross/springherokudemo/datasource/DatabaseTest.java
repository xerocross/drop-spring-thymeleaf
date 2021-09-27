package com.adamfgcross.springherokudemo.datasource;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/drops-data?useSSL=false&serverTimezone=UTC";
        String user = "hbstudent";
        String pass = "hbstudent";
        try {
            System.out.println("Connecting to database: " + jdbcUrl);
            Connection con = DriverManager.getConnection(jdbcUrl, user, pass);
            System.out.println("Connection successful!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
