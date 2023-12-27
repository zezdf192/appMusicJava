package com.example.app.Controller.Admin.ManageAlbum;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Admin.ManagePlaylist.ItemSongInPlaylistController;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemAlbum;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Model;
import com.example.app.Models.Song.Song;
import com.example.app.Views.AdminMenuOptions;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DefaultAlbumController implements Initializable {

    public Text header;
    public VBox songListView;
    public TextField text_search;
    public VBox songSearchView;
    private ItemAlbum album;
    private int albumId;
    public List<ItemSong> listSong = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.X);
        album = Data.getDataGLobal.dataGlobal.getCurrentEditAlbum();
        header.setText(album.getName());
        albumId = album.getId();
        callAPI();
        renderListSong();

        PauseTransition pauseTransition;
        pauseTransition = new PauseTransition(Duration.millis(500));
        pauseTransition.setOnFinished(event -> {
            // Hàm được gọi khi độ trễ kết thúc
            searchDB();
        });

        text_search.setOnKeyPressed((KeyEvent event) -> {
            pauseTransition.playFromStart();
        });
    }

    public void callAPI() {
        try(Connection connection = ConnectDB.getConnection()){

            String query = "SELECT * FROM album_song_user " +
                    "JOIN song ON album_song_user.songId = song.songId " +
                    "JOIN album ON album_song_user.albumId = album.albumId " +
                    "JOIN code ON song.privacy = code.keyCode " +
                    "JOIN user ON user.userId = song.authorId " +
                    "where album_song_user.albumId = " + albumId;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("song.songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                int authorId = resultSet.getInt("authorId");
                int abumId = resultSet.getInt("album.albumId");
                String dateCreated = resultSet.getString("dateCreated");
                String pathSong = resultSet.getString("pathSong");
                String pathImg = resultSet.getString("pathImg");
                String kindOfSong = resultSet.getString("kindOfSong");
                int playListId = resultSet.getInt("playListId");
                int totalLike = resultSet.getInt("totalLike");
                String privacy = resultSet.getString("code.value");
                String desAdmin = resultSet.getString("desAdmin");

                ItemSong itemSong = new ItemSong(id, nameSong, nameAuthor, authorId, abumId, dateCreated, totalLike, pathSong, pathImg, kindOfSong, playListId, privacy, desAdmin);
                listSong.add(itemSong);

            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderListSong() {

        for (int i = 0; i < listSong.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Admin/ManageAlbum/ItemSongInAlbum.fxml"));

            try {
                AnchorPane hBox = fxmlLoader.load();
                ItemSongInAlbumController cic = fxmlLoader.getController();
                cic.setData(listSong.get(i));
                songListView.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void searchDB() {
        songSearchView.getChildren().clear();
        if(text_search.getText().isEmpty()) {
            return;
        }
        try{
            Connection connection = ConnectDB.getConnection();
            String text = text_search.getText();
            String query = "select * from song join user on song.authorId = user.userId and nameSong LIKE '%" + text + "%' ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int songId = resultSet.getInt("songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                String dateCreated =  resultSet.getString("dateCreated");
                String totalLike =  resultSet.getString("totalLike");
                String pathSong =  resultSet.getString("pathSong");
                String pathImg =  resultSet.getString("pathImg");
                String kindOfSong =  resultSet.getString("kindOfSong");
                Song song = new Song(songId, nameSong, nameAuthor, dateCreated, totalLike, pathSong, pathImg, kindOfSong);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/Admin/ManageAlbum/ItemSongSearch.fxml"));
                try {
                    AnchorPane hBox = fxmlLoader.load();
                    ItemSongSearchController cic = fxmlLoader.getController();
                    cic.setData(song);
                    songSearchView.getChildren().add(hBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //text_search.setText("");
            //vbox.getChildren().clear();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
