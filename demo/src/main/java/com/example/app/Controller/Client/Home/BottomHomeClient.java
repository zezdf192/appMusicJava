package com.example.app.Controller.Client.Home;

import com.example.app.Models.Playlist.ListPlayList;
import com.example.app.Controller.Client.Playlist.PlaylistItemController;

import com.example.app.Models.Playlist.PlaylistItem;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BottomHomeClient implements Initializable {
    public HBox playlist_item_layout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playList_Item_Layout();
    }

    private void playList_Item_Layout() {
        List<PlaylistItem> listPlaylist = ListPlayList.ListPlayListGlobal.songList.getListPlaylist_Item();

        for (int i = 0; i < listPlaylist.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/PlaylistView/PlaylistItem.fxml"));

            try {
                AnchorPane hBox = fxmlLoader.load();
                PlaylistItemController cic = fxmlLoader.getController();
                cic.setData(listPlaylist.get(i));
                playlist_item_layout.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
