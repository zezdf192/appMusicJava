package com.example.app.Controller.Client.PlaylistUserCreate;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Admin.ManageAlbum.ItemSongSearchController;
import com.example.app.Controller.Client.Playlist.DetailPlaylistController;
import com.example.app.Controller.Client.Song.SongItemController;
import com.example.app.Controller.Data;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DetailPlaylistUserController extends DetailPlaylistController implements Initializable {
    public TextField text_search;
    public VBox songListView;
    public VBox songSearchView;
    public Text header;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentPlaylist currentPlaylist = Data.getDataGLobal.dataGlobal.getCurrentPlaylist();
        header.setText(currentPlaylist.getNamePlaylist());

        Integer playlistId = currentPlaylist.getPlaylistId();

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;


        callAPI(playlistId);


        // Search Song

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
                System.out.println(pathImg);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItemUserCreate.fxml"));
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

    public void callAPI(Integer playlistId) {
        try(Connection connection = ConnectDB.getConnection()){

//            String query = "SELECT * FROM album_song_user join playlist on album_song_user.playListId = playlist.playlistId JOIN song ON album_song_user.songId = song.songId where album_song_user.playListId = ?";
            String query = "SELECT * FROM album_song_user " +
                    "JOIN playlist ON album_song_user.playListId = playlist.playlistId " +
                    "JOIN song ON album_song_user.songId = song.songId " +
                    "JOIN user ON user.userId = song.authorId " +
                    "WHERE album_song_user.playListId = ? AND album_song_user.role = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, playlistId);
                preparedStatement.setString(2, "R2");

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int songId = resultSet.getInt("songId");
                    String nameSong = resultSet.getString("nameSong");
                    String nameAuthor = resultSet.getString("user.nameUser");
                    String dateCreated =  resultSet.getString("dateCreated");
                    String totalLike =  resultSet.getString("totalLike");
                    String pathSong =  resultSet.getString("pathSong");
                    String pathImg =  resultSet.getString("pathImg");
                    String kindOfSong =  resultSet.getString("kindOfSong");
                    Song song = new Song(songId, nameSong, nameAuthor, dateCreated, totalLike, pathSong, pathImg, kindOfSong);

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItemPlaylistUserCreate.fxml"));
                    try {
                        AnchorPane hBox = fxmlLoader.load();
                        SongItemController cic = fxmlLoader.getController();
                        cic.setData(song);
                        songListView.getChildren().add(hBox);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
