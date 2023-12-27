package com.example.app.Controller;

import com.example.app.Models.Admin.ItemAlbum;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Admin.ItemUser;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;

import java.util.List;

public class Data {
//    private String nameUser;

    private double volumeValue;
    private boolean isRepeat ;
    private boolean isRandom ;

    private List<PlaylistItem> listPlaylistUserLike;
    private Song currentSong;

    private CurrentPlaylist currentPlaylist;

    private ItemUser currentEditUser;

    private ItemSong currentEditSong;

    private ItemAlbum currentEditAlbum;

    private User currentUser;

    public Data() {
        volumeValue = 0.5;
        isRandom = false;
        isRepeat = false;
        currentSong = new Song();
        currentPlaylist = new CurrentPlaylist();
        currentEditUser = new ItemUser();
        currentUser = new User(null, null, null, null, null, null);
    }
    //Remove
    public void setVolumeValue(double volumeValue) {
        this.volumeValue = volumeValue;
    }

//    public String getNameUser() {
//        return nameUser;
//    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public void setCurrentPlaylist(CurrentPlaylist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public double getVolumeValue() {
        return volumeValue;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public Song getSong() {
        return currentSong;
    }

    public CurrentPlaylist getCurrentPlaylist() {
        return currentPlaylist;
    }

    // User
//    public void setCurrentUser (User currentUser) {this.currentUser = currentUser;}
//    public User getCurrentUser() {return currentUser;}


    public ItemUser getCurrentEditUser() {
        return currentEditUser;
    }

    public void setCurrentEditUser(ItemUser currentEditUser) {
        this.currentEditUser = currentEditUser;
    }

    public ItemSong getCurrentEditSong() {
        return currentEditSong;
    }

    public void setCurrentEditSong(ItemSong currentEditSong) {
        this.currentEditSong = currentEditSong;
    }

    public ItemAlbum getCurrentEditAlbum() {
        return currentEditAlbum;
    }

    public void setCurrentEditAlbum(ItemAlbum currentEditAlbum) {
        this.currentEditAlbum = currentEditAlbum;
    }


    public List<PlaylistItem> getListPlaylistUserLike() {
        return listPlaylistUserLike;
    }

    public void setListPlaylistUserLike(List<PlaylistItem> listPlaylistUserLike) {
        this.listPlaylistUserLike = listPlaylistUserLike;
    }

    public class getDataGLobal {
        public static Data dataGlobal = new Data();
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}