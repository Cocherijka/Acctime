package com.example.stas.acctime;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import static com.example.stas.acctime.MyService.con;
import static com.example.stas.acctime.MainActivity.rs;
import static com.example.stas.acctime.MainActivity.stmt;
import static com.example.stas.acctime.MainActivity.finalCount;
/**
 * Created by Stas on 28.04.17.
 */

public class MysqlHelper {
    private static boolean connectionStatus;

    static boolean ipAddr;
    /*  Набор sql команд
                String WorkersCount = "select count(*) from workerslist";
            con = DriverManager.getConnection("jdbc:mysql://82.146.39.250:3306/stas-test","stas","rftgjufjfhj");
            String query = "select id, login, pass from workerslist where pass = " + pass + " and login = '" + login + "'";

            rs = stmt.executeQuery(WorkersCount);


     */


    static boolean isInternetAvailable() {
        System.out.println("try connect");
        /*
        try {
            ipAddr = InetAddress.getByName("google.ru").isReachable(3000); //You can replace it with your name
            System.out.println(ipAddr);
            return ipAddr;
        } catch (Exception e) {
            System.out.println(ipAddr);
            return false;
        }
*/

        URLConnection conn = null;
        try {
            URL url;
            url = new URL("https://www.google.ru");
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        try {
            InputStream in = conn.getInputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean connectionCheck() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://82.146.39.250:3306/stas-test", "stas", "rftgjufjfhj");
            return true;
        } catch (SQLException e) {
            return false;
        }


    }

    static void mysqlConnection() {
        connectionCheck();
        System.out.println("Функция подключения запущена");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер баз данных отключен!");
            e.printStackTrace();
        }
        try {
            String shw = "select count(*) from workerslist";
            stmt = (Statement) con.createStatement();
            rs = stmt.executeQuery(shw);
            while (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total number of workers in the table : " + count);
                finalCount = count;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                rs.close();
            } catch (SQLException se) { /*can't do anything */ }

        }

    }


    static int LogIn(String login, String password) throws SQLException, ClassNotFoundException {
            int id = 0;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://82.146.39.250:3306/stas-test", "stas", "rftgjufjfhj");
            stmt = (Statement) con.createStatement();
            String query = "select id, login, pass from workerslist where pass = '" + password + "' and login = '" + login + "'";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(1);
                System.out.println("id moi" + rs.getInt(1));
                //  String name = rs.getString(2);
                //  String author = rs.getString(3);
                //  System.out.printf("id: %d, name: %s, author: %s %n", id, name, author);
                // System.out.println("id " + id);
            }
        try {
            con.close();
        } catch (SQLException se) { /*can't do anything */ }
        try {
            stmt.close();
        } catch (SQLException se) { /*can't do anything */ }
        try {
            rs.close();
        } catch (SQLException se) { /*can't do anything */ }
        System.out.println("id moi" + id);
            return id;
        }


        static void getFullUserIfo(){
            




        }

    static String getWifi() throws SQLException, ClassNotFoundException {
        String mac = "";
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://82.146.39.250:3306/stas-test", "stas", "rftgjufjfhj");
        stmt = (Statement) con.createStatement();
        String query = "select bssid, name from wifi_all where id = '" + 1 + "'";
        rs = stmt.executeQuery(query);
        rs.next();
            mac = rs.getString(1);
            System.out.println("myMac" + rs.getString(1));
            //  String name = rs.getString(2);
            //  String author = rs.getString(3);
            //  System.out.printf("id: %d, name: %s, author: %s %n", id, name, author);
            // System.out.println("id " + id);
        try {
            con.close();
        } catch (SQLException se) { /*can't do anything */ }
        try {
            stmt.close();
        } catch (SQLException se) { /*can't do anything */ }
        try {
            rs.close();
        } catch (SQLException se) { /*can't do anything */ }
        System.out.println("myMac" + mac);
        return mac;
    }

    static void startWork(int id, String date) throws SQLException {

        try {
            con = DriverManager.getConnection("jdbc:mysql://82.146.39.250:3306/stas-test"
                    + "?useUnicode=true&characterEncoding=UTF-8", "stas", "rftgjufjfhj");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String updateTableSQL = "update m05 set d" + date + " = 'Я8' where id = " + id;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = (PreparedStatement) con.prepareStatement(updateTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
// execute insert SQL stetement
        try {
            preparedStatement .executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            preparedStatement.close();

    }

}

