package org.homeworksubmission.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class userDatabase {
    //loading database information from .env for security
    static Dotenv dotenv = Dotenv.configure().directory("src/main/resources/.env").load();
    static String dbUrl = dotenv.get("DATABASE_URL");
    static String dbUser = dotenv.get("DATABASE_USERNAME");
    static String dbPassword = dotenv.get("DATABASE_PASSWORD");


    public static List<user> getUsers() {
        String query = "SELECT * FROM User";
        List<user> users = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)){
            if (resultSet.next()) {
                user user = new user(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
        }
        public static void main(String[] args) {
        List<user> users = getUsers();
        for (user user : users) {
            System.out.println(user);
        }
        }

    }

