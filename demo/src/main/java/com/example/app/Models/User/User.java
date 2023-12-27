package com.example.app.Models.User;

public class User {
    private String userName;
    private Integer userId;
    private String userEmail;
    private String userGender;

    private String userPassword;

    private String role;

    // Constructor

    public User (String userName, Integer userId, String userEmail, String userGender, String userPassword, String role) {
        this.userName = userName;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userGender = userGender;
        this.userPassword = userPassword;
        this.role = role;
    }
    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}