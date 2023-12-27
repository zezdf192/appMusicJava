package com.example.app.Models.Song;

public class SongItemHome {
    String nameSong;
    String nameAuthor;
    String abum;
    String dateCreated;
    String totalTime;
    String pathSong;

    public SongItemHome(String nameSong, String nameAuthor, String abum, String dateCreated, String totalTime, String pathSong) {
        this.nameSong = nameSong;
        this.nameAuthor = nameAuthor;
        this.abum = abum;
        this.dateCreated = dateCreated;
        this.totalTime = totalTime;
        this.pathSong = pathSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public String getAbum() {
        return abum;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getPathSong() {
        return pathSong;
    }
}
