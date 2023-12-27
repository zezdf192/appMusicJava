package com.example.app.Controller.Client.Album;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Client.Song.SongItemController;
import com.example.app.Controller.Data;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DetailAlbumController implements Initializable {
    public VBox vbox;
    public ScrollPane scroll_songitem;
    public VBox vbox_layout;

    public Label namePlaylist;
    public ImageView img;
    public Label category;
    public Label des_album;


    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentPlaylist currentPlaylist = Data.getDataGLobal.dataGlobal.getCurrentPlaylist();

        namePlaylist.setText(currentPlaylist.getNamePlaylist());
        category.setText(currentPlaylist.getCategory());
        Image image = new Image(currentPlaylist.getThumbnailPlaylist());
        img.setImage(image);

        des_album.setText(currentPlaylist.getDescription());

        Integer playlistId = currentPlaylist.getPlaylistId();

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;

        List<Song> listsongs = ListSongPlaying.SongListGlobal.songList.getListSongs();

        for (int i = 0; i < listsongs.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItem.fxml"));
            try {
                AnchorPane hBox = fxmlLoader.load();
                SongItemController cic = fxmlLoader.getController();
                cic.setData(listsongs.get(i));
                vbox_layout.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
