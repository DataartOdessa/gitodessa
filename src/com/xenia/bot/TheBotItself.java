package com.xenia.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheBotItself extends TelegramLongPollingBot {

    Connection connection = null;

    public TheBotItself(Connection connection) {
        this.connection = connection;
    }

    String[] words;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String get_message_text = update.getMessage().getText();
            get_message_text = get_message_text.replaceAll("[.{},\\[\\]!@#$%^&*()_+|:?>]", "");

            long chat_id = update.getMessage().getChatId();

            SendMessage message = null;
            try {
                words = formArr(get_message_text);
                for (String s : words) {
                    message = new SendMessage()
                            .setChatId(chat_id)
                            .setText(selectPhrase());

                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                execute(message);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "LitsBot";
    }

    @Override
    public String getBotToken() {
        return "432046525:AAFoe2mbHIbQJiHP6eYvPgVf4d4S31JnhpA";
    }

    public int levenstein(String s1, String s2) {
        int[] d1;
        int[] d2 = new int[s2.length() + 1];

        for (int i = 0; i <= s2.length(); i++) {
            d2[i] = i;
        }

        for (int i = 1; i <= s1.length(); i++) {
            d1 = d2;
            d2 = new int[s2.length() + 1];
            for (int j = 0; j <= s1.length(); j++) {
                if (j == 0) d2[j] = i;
                else {
                    int cost = (s1.charAt(i - 1) != s2.charAt(j - 1)) ? 1 : 0;
                    if (d2[j - 1] < d1[j] && d2[j - 1] < d1[j - 1] + cost)
                        d2[j] = d2[j - 1] + 1;
                    else if (d1[j] < d1[j - 1] + cost)
                        d2[j] = d1[j] + 1;
                    else
                        d2[j] = d1[j - 1] + cost;
                }
            }
        }
        System.out.println("distance: " + d2[s2.length()]);
        return d2[s2.length()];
    }


    public String[] formArr(String text) {
        words = text.replaceAll("[.{},\\[\\]!@#$%^&*()_+|:?>]", " ").split(" ");
        System.out.println(Arrays.asList(words));
        return words;
    }

    public int dbArrLength() throws SQLException {
        Statement stat = connection.createStatement();
        ResultSet rs = stat.executeQuery("Select count(*) from answer, keyword where keyword.id = 1 and answer.keywords = keyword.id");
        int count = 0;
        while (rs.next()) try {
            count = rs.getInt(1);
            System.out.println(rs.getInt(1));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        stat.closeOnCompletion();
        System.out.println("length = " + count);
        return count;
        //TODO delete this line
    }

    private int switchState() throws SQLException {
        Statement stmt = connection.createStatement();
        int state = 1;
        for (int i = 0; i < words.length; i++) {
            ResultSet rs = stmt.executeQuery("select state from answer, keyword\n" +
                    "where '" + words[i] + "' = keyword.word\n" +
                    "and answer.keywords = keyword.id \n");
            while (rs.next()) {
                state = rs.getInt(1);
            }
        }
        System.out.println("state = " + state);
        return state;
    }

    private String selectPhrase() throws SQLException, IOException, ClassNotFoundException {
        Statement stmt = connection.createStatement();
        String[] ans = {};
        List<String> list = new ArrayList<>();

        int a;
        for (int i = 0; i < words.length; i++) {
           ResultSet rs = stmt.executeQuery("with temp_tbl as(\n" +
                   "select array_to_string(ARRAY[author, caption, description], '. ')\n" +
                   "from genre, book, keyword\n" +
                   "where '" + words[i] + "' ~* genre.genre \n" +
                   "and genre.id = any(book.genre)\n" +
                   "UNION\n" +
                   "select answer from keyword, answer, genre, book\n" +
                   "where '" + words[i] + "' ~* keyword.word\n" +
                   "and answer.keywords = keyword.state\n" +
                   " )\n" +
                   "select * from temp_tbl");
            while (rs.next()) {
                list.add(rs.getString(1));
                ans = list.toArray(new String[list.size()]);
            }
        }
        stmt.close();
        System.out.println("answer = " + list);
        a = (int) (Math.random() * ans.length);
        return ans[a];
    }
}