package com.example.app.Models.Admin;

public class ItemAlbum {
    private int id;

    private String thumbnail;
    private String name;
    private int authorId;
    private int quantitySong;
    private String description;

    public ItemAlbum () {

    }


    public ItemAlbum(int id, String thumbnail, String name, int authorId, int quantitySong, String description) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.authorId = authorId;
        this.quantitySong = quantitySong;
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getName() {
        return name;
    }

    public int getQuantitySong() {
        return quantitySong;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }
}