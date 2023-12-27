package com.example.app.Models.Playlist;

import com.example.app.Models.Song.ListSongPlaying;

public class CurrentPlaylist {
    int playlistId;
    String thumbnailPlaylist;
    String category;
    String namePlaylist;
    int authorId;
    int quantitySong;

    String description;
    //String urlImage;


    public CurrentPlaylist() {
        // Default constructor
    }

    public CurrentPlaylist(PlaylistItem playlistItem) {
        this.playlistId = playlistItem.getPlaylistId();
        this.thumbnailPlaylist = playlistItem.thumbnailPlaylist;
        this.category = playlistItem.getCategory();
        this.namePlaylist = playlistItem.namePlaylist;
        this.authorId = playlistItem.getAuthorId();
        this.quantitySong = playlistItem.getQuantitySong();
        this.description = playlistItem.description;
    }

    public CurrentPlaylist(int playlistId, String thumbnailPlaylist, String category, String namePlaylist, int authorId, int quantitySong, String description) {
        this.playlistId = playlistId;
        this.thumbnailPlaylist = thumbnailPlaylist;
        this.category = category;
        this.namePlaylist = namePlaylist;
        this.authorId = authorId;
        this.quantitySong = quantitySong;
        this.description = description;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getThumbnailPlaylist() {
        return thumbnailPlaylist;
    }

    public String getCategory() {
        return category;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getQuantitySong() {
        return quantitySong;
    }

    public String getDescription() {
        return description;
    }


}
