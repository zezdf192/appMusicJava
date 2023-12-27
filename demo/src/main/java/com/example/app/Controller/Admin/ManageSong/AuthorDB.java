package com.example.app.Controller.Admin.ManageSong;

public class AuthorDB {
    String name;
    int id;

    public AuthorDB(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

