package com.example.app.Models.Playlist;

public class PlaylistItem {

    int playlistId;
    String thumbnailPlaylist;
    String category;
    String namePlaylist;
    int authorId;
    int quantitySong;

    String description;
    //String urlImage;


    //String urlImage;

    String desPlaylist;


    public PlaylistItem() {
        // Default constructor
    }


    public PlaylistItem(int playlistId, String thumbnailPlaylist, String category, String namePlaylist, int authorId, int quantitySong, String description) {
        this.playlistId = playlistId;
        this.thumbnailPlaylist = thumbnailPlaylist;
        this.category = category;
        this.namePlaylist = namePlaylist;
        this.authorId = authorId;
        this.quantitySong = quantitySong;
        this.description = description;
    }

//    public PlaylistItem(String urlImage, String namePlaylist, String desPlaylist) {
//        this.urlImage = urlImage;
//        this.namePlaylist = namePlaylist;
//        this.desPlaylist = desPlaylist;
//    }


    public int getPlaylistId() {
        return playlistId;
    }

    public String getThumbnailPlaylist() {
        return thumbnailPlaylist;
    }

    public String getCategory() {
        return category;
    }


    public PlaylistItem( String namePlaylist, String desPlaylist) {
       // this.urlImage = urlImage;
        this.namePlaylist = namePlaylist;
        this.desPlaylist = desPlaylist;
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
    public String getDesPlaylist() {
        return desPlaylist;

    }
}
