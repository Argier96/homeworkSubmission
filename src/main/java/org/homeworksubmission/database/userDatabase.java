package org.homeworksubmission.database;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userDatabase {
    //loading database information from .env for security
    static Dotenv dotenv = Dotenv.configure().directory("src/main/resources/.env").load();
    static String dbUrl = dotenv.get("DATABASE_URL");
    static String dbUser = dotenv.get("DATABASE_USERNAME");
    static String dbPassword = dotenv.get("DATABASE_PASSWORD");



    // Method to add a user to the database
    public static boolean addUser(String username, String password, String email, int role) {
        String sql = "INSERT INTO users (username, password, email, role_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generate hash
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // hashing the password for security
            stmt.setString(3, email);
            stmt.setInt(4, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User added successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
        return false;
    }


    public static List<user> getUsers() {
        String query = "SELECT * FROM users";
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
                        resultSet.getInt("role_id")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
        }
    public static String checkLogin(String userName, String password) {
        String sql = "SELECT * FROM users WHERE username = ?"; // SQL query to get user by username

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName); // Setting the username in the SQL query

            try (ResultSet rs = stmt.executeQuery()) { // Correctly execute query only once
                if (rs.next()) {
                    // Retrieve the user details from the database
                    String storedHash = rs.getString("password"); // The hashed password stored in DB
                    String username = rs.getString("username");
                    String passwordHash = rs.getString("password");
                    String email = rs.getString("email");
                    int roleInt = rs.getInt("role_id");
                    String role = "";
                    switch (roleInt) {
                        case 1:   role ="admin";
                        break;
                        case 2:   role ="teacher";
                        break;
                        case 3:   role ="student";
                        break;
                    }

                    // Check if the entered password matches the stored hash
                    boolean isMatch = BCrypt.checkpw(password, storedHash);
                    if (isMatch) {
                        return role;
                    } else {
                        return "Incorrect username or password " ;
                    }
                } else {
                    return "User not found"; // If no user was found in the DB
                }
            }
        } catch (SQLException e) {
            // Handle database connection errors or SQL issues
            return "Error fetching user: " + e.getMessage();
        }

    }
        public static void main(String[] args) {

        }

    }

