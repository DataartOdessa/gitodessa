package com.xenia.bot;
import com.xenia.bot.db.PostgresqlDBConnector;
import jdk.nashorn.internal.scripts.JD;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //PostgresqlDBConnector postgresqlDBConnector;


        Connection connection = PostgresqlDBConnector.getInstance().getConnection();
//        PostgresqlDBConnector post2 = PostgresqlDBConnector.getInstance();
//
//        System.out.println(post1.getName());
//        System.out.println(post2.getName());

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new TheBotItself(connection));

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }



    }
}
