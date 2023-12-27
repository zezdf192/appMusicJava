package com.example.app.Controller.Admin;


import com.example.app.Models.Song.Song;

import java.util.ArrayList;
import java.util.List;

public class ListSongCheck {
    private List<Song> listSongCheck;

    public ListSongCheck() {

        listSongCheck = new ArrayList<>();
    }

    public void addSong(Song song) {
        listSongCheck.add(0, song);
    }




    // Lấy bài hát theo chỉ mục
    public Song getSong(int index) {
        return listSongCheck.get(index);
    }

    // Lấy số lượng bài hát trong danh sách
    public int getCount() {
        return listSongCheck.size();
    }

    public List<Song> getListSongCheck() {
        return listSongCheck;
    }

    public void removeListSong() {
        listSongCheck.clear();
    }

    public void removeSong(int songId) {
        for (int i = 0; i < listSongCheck.size(); i++) {
            if (listSongCheck.get(i).getSongId() == songId) {
                listSongCheck.remove(i);
                return;
            }
        }
    }

    public class ListSongCheckGlobal {
        public static ListSongCheck songList = new ListSongCheck();
    }
}
