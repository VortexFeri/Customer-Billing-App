package db;

import java.sql.*;
import java.util.logging.Logger;

import static utils.macros.log;


public class db {
    public static boolean loggedIn = false;
    public static Connection connection;

    public static boolean isOpen() {
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return false;
        }
        return true;
    }

    public static void connect(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            loggedIn = true;
            log("Logged in successfully as: " + username);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(db.class.getName()).severe(e.toString());
        }
    }

    public static ResultSet getTable(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(String.format("SELECT * FROM %s", tableName));
    }

    public static ResultSet getTable(String tableName, String[] columns) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(String.format("SELECT %s FROM %s", String.join(", ", columns), tableName));
    }

    public static String getString(String tableName, String column, int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM %s WHERE id = %d", column, tableName, id));
        return resultSet.getString(column);
    }

    public static int getInt(String tableName, String column, int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM %s WHERE id = %d", column, tableName, id));
        return resultSet.getInt(column);
    }

    public static float getFloat(String tableName, String column, int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM %s WHERE id = %d", column, tableName, id));
        return resultSet.getFloat(column);
    }

    public static ResultSet getDistinct(String tableName, String column) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(String.format("SELECT DISTINCT %s FROM %s", column, tableName));
        } catch (SQLException e) {
            Logger.getLogger(db.class.getName()).severe(e.toString());
            return null;
        }
    }

    public static void updateOne(String tableName, int id, String col, String newValue) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("UPDATE %s SET %s = %s WHERE id = %d", tableName, col, newValue, id));
        } catch (SQLException e) {
            Logger.getLogger(db.class.getName()).severe(e.toString());
        }
    }
    public static void updateMany(String tableName, int id, String[] cols, String[] newValues) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("UPDATE %s SET %s = %s WHERE id = %d", tableName, String.join(", ", cols), String.join(", ", newValues), id));
        } catch (SQLException e) {
            Logger.getLogger(db.class.getName()).severe(e.toString());
        }
    }
}