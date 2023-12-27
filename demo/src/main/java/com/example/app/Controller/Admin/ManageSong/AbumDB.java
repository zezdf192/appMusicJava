package com.example.app.Controller.Admin.ManageSong;

public class AbumDB {
    String name;
    int id;

    public AbumDB(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
