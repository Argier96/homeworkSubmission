package org.homeworksubmission.database;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.homeworksubmission.database.userDatabase.addUser;
import static org.homeworksubmission.database.userDatabase.deleteUser;
import static org.junit.jupiter.api.Assertions.*;

class userDatabaseTest {

    static Dotenv dotenv = Dotenv.configure().directory("src/main/resources/.env").load();
    static String dbUrl = dotenv.get("DATABASE_URL");
    static String dbUser = dotenv.get("DATABASE_USERNAME");
    static String dbPassword = dotenv.get("DATABASE_PASSWORD");

    // Helper method to execute SQL queries
    private void executeUpdate(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters dynamically
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate(); // Execute the query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {

    }
    @Test
    void testGetAllUsers() {
        List<user> users = userDatabase.getUsers();

        //verify if users are being fetched or not
        assertFalse(users.isEmpty(), "The list of users is available");
        // Verify the details of the first user
        user firstUser = users.getFirst();
        assertEquals("admin", firstUser.getFirstName(), "First name should be admin");
        assertEquals("admin", firstUser.getLastName(), "Last name should be admin");
        assertEquals("admin", firstUser.getUserName(), "Username should be admin");
        assertEquals("admin@admin.fi", firstUser.getEmail(), "Email should be admin@admin.fi");
        assertEquals(1, firstUser.getRole(), "Role ID should be 1");

    }

    @Test
    void testDeleteUser_Success() {
        // Insert a test user
        addUser("testUser","testUser","testUser","testUser","testUser",1);


        //Test deleting recently created user
        boolean result = deleteUser("testUser");
        assertTrue(result, "deleted user testUser");

        // Test deleting a non-existent user
        boolean isNonExistentUserDeleted = deleteUser("riteshg");
        assertFalse(isNonExistentUserDeleted, "Deleting a non-existent user should return false");
    }

    @Test
    void testDeleteUser_NonExistentUser() {
        // Insert a test user
        executeUpdate("INSERT INTO users (username) VALUES (?)", "testUser");

        // Test deleting a user that does not exist
        boolean result = deleteUser("nonExistentUser");
        assertFalse(result, "Deleting a non-existent user should return false");
    }

    @Test
    void testCheckLogin_Success() {
        String loginResult = userDatabase.checkLogin("admin","admin112");
        assertTrue(loginResult.equals("admin"), "Login should return 'admin' for valid admin credentials");
    }

    @Test
    void testCheckLogin_NonExistentUser() {
        String loginResult = userDatabase.checkLogin("riteshg","riteshg112");
        assertFalse(loginResult.isEmpty(), "Login should return '' for non-existent user");
    }

    @Test
    void testCheckLogin_WrongPassword() {
        String loginResult = userDatabase.checkLogin("admin","admin123");
        assertFalse(loginResult.isEmpty(), "Login should return '' for wrong password");
    }
    @Test
    void testCheckLogin_WrongUser() {
        String loginResult = userDatabase.checkLogin("admin22","admin123");
        assertFalse(loginResult.isEmpty(), "Login should return '' for wrong user");
    }

}