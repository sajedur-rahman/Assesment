/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SRTareq
 */
public class DBConnection {

    private Connection con = null;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cimsdb", "root", "");

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return con;

    }
}
