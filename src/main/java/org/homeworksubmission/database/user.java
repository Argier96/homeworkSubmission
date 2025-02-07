package org.homeworksubmission.database;

public class user {
    String userName;
    String password;
    String email;
    int role;

    //Constructor for user
    public user(String userName, String password, String email, int role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    //getter and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // Convert role integer to readable string
    public String getRoleAsString() {
        return switch (role) {
            case 1 -> "Admin";
            case 2 -> "Teacher";
            case 3 -> "Student";
            default -> "Student";
        };


    }
}