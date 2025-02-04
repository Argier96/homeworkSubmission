package org.homeworksubmission.database;

public class user {
    String userName;
    String password;
    String email;
    String role;

    //Constructor for user
    public user(String userName, String password, String email, String role) {
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    //override to string
    @Override
    public String toString() {
        return "User{username='" + userName +"', password'"+password+ "', email='" + email + "', role='" + role + "'}";

    }
}
