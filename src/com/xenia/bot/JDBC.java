package com.xenia.bot;

import com.xenia.bot.db.PostgresqlDBConnector;

import java.sql.*;

public class JDBC {
    static Connection dbcon;
    static Statement stat;
    PreparedStatement preparedStat;

    JDBC() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (dbcon == null) {
        try {

                String user = "postgres";
                String pass = "masterkey";
                Class.forName("org.postgresql.Driver").newInstance();
//            stat = dbcon.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                dbcon = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/bot_tongue", user, pass);

                //preparedStat = dbcon.prepareStatement("SELECT * FROM answers");
//            String b = "myname";
//            preparedStat.setString(1, b);
//            preparedStat.executeUpdate();

//            stat = dbcon.createStatement();
                // ResultSet result =  stat.executeQuery("SELECT * FROM answer");
//            String sql = "select * from .... ";
//            ResultSet resultSet = stat.executeQuery(sql);
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("name"));
//            }
//            resultSet.close();
//            stat.close();
                System.out.println("JDBC success");
            } catch(ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static Connection getInstance() throws SQLException, ClassNotFoundException {
        if (dbcon == null) {
            //dbcon = new JDBC();
        }
        return dbcon;
    }
    public boolean doesMatchKeyword (String [] arr) throws SQLException {
        for (int i = 0; i < arr.length; i++ ) {
            preparedStat.executeQuery("SELECT * FROM keywords WHERE keyword = "+ arr[i]+"");
        }
        return true;
    }


/*    public String[][] getSql (String sql) throws SQLException {
        ResultSet resultSet = stat.executeQuery(sql);
        int col = resultSet.getMetaData().getColumnCount();
        resultSet.last();
        int row = resultSet.getRow();
        resultSet.first();
        String[][] resTbl = new String[row+1][col];
        int j = 0;
        for (int i = 1; i <= col; i++) {
            resTbl[j][i-1] = resultSet.getMetaData().getColumnName(i);
        }
        resultSet.beforeFirst();
        while (resultSet.next()) {
            j++;
            for (int i = 1; i <= col; i++) {
                String s = resultSet.getString(i);
                resTbl[j][i-1] = s;
            }
        }
        stat.closeOnCompletion();
        return resTbl;
    }*/
}
