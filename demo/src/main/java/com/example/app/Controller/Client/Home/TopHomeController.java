package com.example.app.Controller.Client.Home;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Model;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TopHomeController implements Initializable {
    public AnchorPane box_1;
    public AnchorPane box_2;
    public AnchorPane box_3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box_1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ListSongPlaying.SongListGlobal.songList.removeListSong();

                CurrentPlaylist currentPlaylist = new CurrentPlaylist(1, "https://res.cloudinary.com/djfpcyyfe/image/upload/v1701788708/im7lnao44vyhirjqucw7.jpg", "", "Tuyển Tập Nhạc Sơn Tùng M-TP",  2,  6,  "Được bình chọn yêu thích nhất của Sơn Tùng M-TP");
                Data.getDataGLobal.dataGlobal.setCurrentPlaylist(currentPlaylist);

                try{
                    Connection connection = ConnectDB.getConnection();
                    String query = "select * from song join album join user on song.albumId = album.albumId and user.userId = song.authorId and album.albumId =" + 1;
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
                        ListSongPlaying.SongListGlobal.songList.addSong(song);
                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }

                //Đặt lại Playlist
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/Fxml/Client/DetailAlbum.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the new BottomClient as the bottom of the o
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DETAIL);
                borderPane.setCenter(viewBottomClient);
            }
        });
        box_2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ListSongPlaying.SongListGlobal.songList.removeListSong();

                CurrentPlaylist currentPlaylist = new CurrentPlaylist(3, "https://res.cloudinary.com/djfpcyyfe/image/upload/v1701789896/qmqblui00y2ewkg7xtqs.jpg", "", "Tuyển Tập Nhạc của Karik",  4,  3,  "Top bài hát hay nhất năm 2018");
                Data.getDataGLobal.dataGlobal.setCurrentPlaylist(currentPlaylist);

                try{
                    Connection connection = ConnectDB.getConnection();
                    String query = "select * from song join album join user on song.albumId = album.albumId and user.userId = song.authorId and album.albumId =" + 3;
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
                        ListSongPlaying.SongListGlobal.songList.addSong(song);
                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }

                //Đặt lại Playlist
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/Fxml/Client/DetailAlbum.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the new BottomClient as the bottom of the o
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DETAIL);
                borderPane.setCenter(viewBottomClient);
            }
        });
        box_3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ListSongPlaying.SongListGlobal.songList.removeListSong();

                CurrentPlaylist currentPlaylist = new CurrentPlaylist(4, "https://res.cloudinary.com/djfpcyyfe/image/upload/v1701790326/duhffmibu7pnyktyynfv.jpg", "", "Những ca khúc hay nhất của Hồ Ngọc Hà",  5,  3,  "Top bài hát hay nhất năm 2016");
                Data.getDataGLobal.dataGlobal.setCurrentPlaylist(currentPlaylist);

                try{
                    Connection connection = ConnectDB.getConnection();
                    String query = "select * from song join album join user on song.albumId = album.albumId and user.userId = song.authorId and album.albumId =" + 4;
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
                        ListSongPlaying.SongListGlobal.songList.addSong(song);
                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }

                //Đặt lại Playlist
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/Fxml/Client/DetailAlbum.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the new BottomClient as the bottom of the o
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DETAIL);
                borderPane.setCenter(viewBottomClient);
            }
        });
    }
}
