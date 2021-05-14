package edu.wpi.teamC.repository;

import edu.wpi.teamC.SetupDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {

        // static variable single_instance of the singleton
        private static ConnectionSingleton single_instance = null;

        public String s;
        public Connection conn;

        // private constructor restricted to this class itself
    //function to switch to embedded
        private ConnectionSingleton()
        {
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Apache Derby Driver not found");
                return;
            }
            try {
                conn = DriverManager.getConnection("jdbc:derby:cDB;create=true");
                s = "Connection To Embedded Database Established";
                System.out.println(s);
            } catch (SQLException e) {
                s = "Connection failed. Check output console";
                conn = null;
                System.out.println(s);
            }
        }

        public void embeddedToRemote(String location, String port, String dbName){
            try{
                try {
                    Class.forName("org.apache.derby.jdbc.ClientDriver");
                } catch (ClassNotFoundException e) {
                    System.out.println("Apache Derby Driver not found");
                    return;
                }
                conn = DriverManager.getConnection("jdbc:derby://"+location+":"+port+"/"+ dbName);
                s = "Connection to Remote Database Established";
                SetupDB.main(new String[0]);
                System.out.println(s);
            }
            catch (SQLException ex) {
                try {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                } catch (ClassNotFoundException e) {
                    System.out.println("Apache Derby Driver not found");
                    return;
                }
                try {
                    conn = DriverManager.getConnection("jdbc:derby:cDB;create=true");
                    s = "Connection To Embedded Database Established";
                    System.out.println(s);
                } catch (SQLException e) {
                    s = "Connection failed. Check output console";
                    conn = null;
                    System.out.println(s);
                }
            }
        }

        public void remoteToEmbedded(){
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Apache Derby Driver not found");
                return;
            }
            try {
                conn = DriverManager.getConnection("jdbc:derby:cDB;create=true");
                s = "Connection To Embedded Database Established";
                System.out.println(s);
            } catch (SQLException e) {
                s = "Connection failed. Check output console";
                conn = null;
                System.out.println(s);
            }
        }
    // static method to create instance of singleton class
    public static ConnectionSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new ConnectionSingleton();

        return single_instance;
    }
}


