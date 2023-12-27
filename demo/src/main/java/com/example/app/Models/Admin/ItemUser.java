package com.example.app.Models.Admin;

public class ItemUser {
    private int id;
    private String name;
    private String email;
    private String role;
    private String gender;

    public ItemUser() {

    }

    public ItemUser(int id, String name, String email, String role, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }
}
