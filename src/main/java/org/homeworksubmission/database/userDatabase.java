package org.homeworksubmission.database;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class userDatabase {
    //loading database information from .env for security
    static Dotenv dotenv = Dotenv.configure().directory("src/main/resources/.env").load();
    static String dbUrl = dotenv.get("DATABASE_URL");
    static String dbUser = dotenv.get("DATABASE_USERNAME");
    static String dbPassword = dotenv.get("DATABASE_PASSWORD");

    //delete user from database
    public static boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            // executeUpdate returns the number of rows affected, so we check if it's > 0
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();  // Optionally log the exception
            return false;
        }
    }


    //check if user exists or not
    public static boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ? or email =? ";
        try(Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        }
        catch (SQLException e) {
           return false;
        }

    }

    // Method to add a user to the database
    public static boolean addUser(String firstName, String lastName,String username, String password, String email, int role) {
        String sql = "INSERT INTO users (first_name,last_name,username, password, email, role_id) VALUES (?, ?, ?, ?,?,?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generate hash
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, username);
            stmt.setString(4, hashedPassword); // hashing the password for security
            stmt.setString(5, email);
            stmt.setInt(6, role);

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
            while (resultSet.next()) {
                user user = new user(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
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

    public static boolean updateUser(String username, String firstName, String lastName, String newUsername, String password, String email, int role) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, email = ?, role_id = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Check if the password is provided; if not, don't change it.
            String hashedPassword = (password != null && !password.isEmpty()) ? BCrypt.hashpw(password, BCrypt.gensalt()) : null;

            stmt.setString(1, (firstName != null && !firstName.isEmpty()) ? firstName : getCurrentValue(username, "first_name"));
            stmt.setString(2, (lastName != null && !lastName.isEmpty()) ? lastName : getCurrentValue(username, "last_name"));
            stmt.setString(3, (newUsername != null && !newUsername.isEmpty()) ? newUsername : getCurrentValue(username, "username"));
            stmt.setString(4, (hashedPassword != null) ? hashedPassword : getCurrentValue(username, "password"));
            stmt.setString(5, (email != null && !email.isEmpty()) ? email : getCurrentValue(username, "email"));
            stmt.setInt(6, role != 0 ? role : Integer.parseInt(Objects.requireNonNull(getCurrentValue(username, "role_id"))));
            stmt.setString(7, username);  // This identifies the user to update.

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if the update was successful
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    public static String getCurrentValue(String username, String columnName) {
        String sql = "SELECT " + columnName + " FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(columnName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching current value: " + e.getMessage());
        }
        return null;  // Return null if value could not be fetched
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
            System.out.println(checkLogin("admin", "admin112"));
        }

    }

