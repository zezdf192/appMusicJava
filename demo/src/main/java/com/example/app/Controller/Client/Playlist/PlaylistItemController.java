package com.example.app.Controller.Client.Playlist;


import com.example.app.ConnectDB.ConnectDB;

import com.example.app.Controller.Data;
import com.example.app.Models.Model;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PlaylistItemController implements Initializable {
    //public Image url_Img;
    public int playlistId;
    public Label namePlaylist;
    public Text des_Playlist;
    public ImageView img;
    public AnchorPane container;

    String thumbnailPlaylist;
    String category;
    int authorId;
    int quantitySong;
    String description;



    public void setData (PlaylistItem playlistItem) {

        Task<Image> loadImageTask = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return new Image(playlistItem.getThumbnailPlaylist());
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image loadedImage = loadImageTask.getValue();
            img.setImage(loadedImage);

            // Update other UI elements after loading the image
            namePlaylist.setText(playlistItem.getNamePlaylist());
            des_Playlist.setText(playlistItem.getDescription());

            // Update other playlist information
            playlistId = playlistItem.getPlaylistId();
            thumbnailPlaylist = playlistItem.getThumbnailPlaylist();
            category = playlistItem.getCategory();
            authorId = playlistItem.getAuthorId();
            quantitySong = playlistItem.getQuantitySong();
            description = playlistItem.getDescription();
        });

        // Handle exceptions if the image loading fails
        loadImageTask.setOnFailed(event -> {
            Throwable exception = loadImageTask.getException();
            exception.printStackTrace();
            // Handle the exception as needed
        });

        // Start the image loading task
        new Thread(loadImageTask).start();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ListSongPlaying.SongListGlobal.songList.removeListSong();

                CurrentPlaylist currentPlaylist = new CurrentPlaylist(playlistId, thumbnailPlaylist, category, namePlaylist.getText(),  authorId,  quantitySong,  description);
                Data.getDataGLobal.dataGlobal.setCurrentPlaylist(currentPlaylist);

                try{
                    Connection connection = ConnectDB.getConnection();
                    //String query = "select * from song join playlist join user on song.playListId = playlist.playlistId and user.userId = song.authorId and playlist.playlistId =" + playlistId;
                    String query = "select * from album_song_user as asu join playlist join user join song on asu.songId = song.songId and asu.playListId = playlist.playlistId and user.userId = song.authorId and asu.playlistId =" + playlistId;

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

                loader.setLocation(getClass().getResource("/Fxml/Client/DetailPlaylist.fxml"));

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