package com.example.app.Models.Admin;

public class ItemSong {
    int songId;
    String nameSong;
    int authorId;
    int abumId;

    String nameAuthor;

    String dateCreated;
    int totalLike;
    String pathSong;
    String pathImg;

    String kindOfSong;

    int playlistId;
    String privacy;
    String desAdmin;

    public ItemSong(int songId, String nameSong, String nameAuthor, int authorId, int abumId, String dateCreated, int totalLike, String pathSong, String pathImg, String kindOfSong, int playlistId, String privacy, String desAdmin) {
        this.songId = songId;
        this.nameSong = nameSong;
        this.authorId = authorId;
        this.nameAuthor = nameAuthor;
        this.abumId = abumId;
        this.dateCreated = dateCreated;
        this.totalLike = totalLike;
        this.pathSong = pathSong;
        this.pathImg = pathImg;
        this.kindOfSong = kindOfSong;
        this.playlistId = playlistId;
        this.privacy = privacy;
        this.desAdmin = desAdmin;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public ItemSong() {

    }

    public int getSongId() {
        return songId;
    }

    public String getNameSong() {
        return nameSong;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getAbumId() {
        return abumId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public String getPathSong() {
        return pathSong;
    }

    public String getPathImg() {
        return pathImg;
    }

    public String getKindOfSong() {
        return kindOfSong;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getDesAdmin() {
        return desAdmin;
    }
}
