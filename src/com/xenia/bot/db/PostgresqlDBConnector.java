package com.xenia.bot.db;

import java.sql.*;

public class PostgresqlDBConnector {

    static PostgresqlDBConnector dbcon;
    //public static Statement stat;
    //PreparedStatement preparedStat;
    Connection connection;

    public static PostgresqlDBConnector singleObj;

    private PostgresqlDBConnector() {
        try {
            System.out.println("Trying to connect to DB");
            String user = "postgres";
            String pass = "masterkey";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/bot_tongue", user, pass);
            System.out.println("Connected!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static PostgresqlDBConnector getInstance() throws SQLException, ClassNotFoundException {
        if (dbcon == null) {
            dbcon = new PostgresqlDBConnector();
        }
        return dbcon;
    }

    public Connection getConnection() {
        return connection;
    }
}
