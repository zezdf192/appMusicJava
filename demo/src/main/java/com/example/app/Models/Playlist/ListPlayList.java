package com.example.app.Models.Playlist;



import java.util.ArrayList;
import java.util.List;

public class ListPlayList {
    private List<PlaylistItem> listPlaylist_Item;

    public ListPlayList() {
        listPlaylist_Item = new ArrayList<>();
    }

    public void addListPlayList(PlaylistItem song) {
        listPlaylist_Item.add(0, song);
    }

    public void removeSongList () {
        listPlaylist_Item.clear();
    }

    public List<PlaylistItem> getListPlaylist_Item() {
        return listPlaylist_Item;
    }

    public class ListPlayListGlobal {
        public static ListPlayList songList = new ListPlayList();
    }
    public class ListPlaylistCurrentUser {
        public static ListPlayList songList = new ListPlayList();
    }
    public class ListPlaylistLiked {
        public static ListPlayList songList = new ListPlayList();
    }
}
