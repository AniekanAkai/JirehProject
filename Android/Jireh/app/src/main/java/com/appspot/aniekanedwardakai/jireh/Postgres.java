package com.appspot.aniekanedwardakai.jireh;

/**
 * Created by Teddy on 1/9/2016.
 */
import android.util.Log;

import java.sql.*;

public class Postgres {

    private static Connection db;

    public static boolean connect(){
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println("ERROR: "+e.getMessage()+"__END__");
        }

        String url = "jdbc:postgresql://pellefant-01.db.elephantsql.com:5432/zmzglpce";
        String username = "zmzglpce";
        String password = "f3l9usywRliS9YxABTedu91vR-4ZRsRi";
        int rowCount = 0;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            db = conn;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM jdbctest");

            while (rs.next()) {
                System.out.print("Column ID returned ");
                System.out.println(rs.getString(1));
                System.out.print("Column Username returned ");
                System.out.println(rs.getString(2));
                rowCount++;
            }
            rs.close();
            st.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return (rowCount>0);
    }

    public static boolean addNewUser(User user){

        String insertSqlStatement= "insert into Users values (%s, %tD, %s, %d, %s, %f)";
        insertSqlStatement = String.format(insertSqlStatement, user.getFullname(),
                user.getDateOfBirth(), user.getCurrentLocation().toString(),Integer.parseInt(user.getPhoneNumber()),
                user.getEmail(), user.getCurrentAverageRating());
        try {
            Statement st = db.createStatement();
            st.executeUpdate(insertSqlStatement);
            db.commit();
            st.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean isConnected(){
        boolean connected = false;
        try {
            connected = !db.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return connected;
    }

    public static boolean disconnect(){
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        int count = 0;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println("ERROR: "+e.getMessage()+"__END__");
        } catch (InstantiationException e) {
            System.out.println("ERROR: " + e.getMessage() + "__END__");
        } catch (IllegalAccessException e) {
            System.out.println("ERROR: " + e.getMessage() + "__END__");
        }

        String url = "jdbc:mysql://104.196.110.146:3306/JirehSQL";
        String username = "root";
        String password = "jirehmysql1!";

        try {
            Connection db = DriverManager.getConnection(url, username, password);
            Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM jdbctest");
            while (rs.next()) {
                System.out.print("Column ID returned ");
                System.out.println(rs.getString(1));
                System.out.print("Column Username returned ");
                System.out.println(rs.getString(2));
                count++;
            }
            rs.close();
            st.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Row Count = "+count);
    }

    public static boolean confirmUserCredentials(String email, String username, String password) {
        int resultCount= 0;
        String sqlStatement ="";
        try {
            Statement st = db.createStatement();
            if(email.equals("") && !username.equals("")) {
                sqlStatement = String.format("SELECT * FROM Users where username=%s AND password=%s", username, password);
            }else if(!email.equals("") && username.equals("")){
                sqlStatement = String.format("SELECT * FROM Users where email=%s AND password=%s", username, password);
            }

            ResultSet rs = st.executeQuery(sqlStatement);
            while (rs.next()) {
                resultCount++;
            }
            if(resultCount>1) Log.w("Jireh", "Confirming user credentials - duplicate username/email in DB");
            rs.close();
            st.close();
        }catch (java.sql.SQLException e){
            e.printStackTrace();
            Log.e("Jireh", "Confirming user login credentials- SQL Exception \n".concat(e.getMessage()));
            return false;
        }
        return (resultCount>0);
    }
}