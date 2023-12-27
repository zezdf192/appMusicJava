package com.example.app.Controller.Client;


import com.example.app.ConnectDB.ConnectDB;

import com.example.app.Controller.Client.Playlist.PlaylistItemController;
import com.example.app.Controller.Client.Song.SongItemController;
import com.example.app.Controller.Data;
import com.example.app.Models.Model;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.User.User;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.Song;

import java.io.IOException;
import java.util.ArrayList;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;



public class CollectionController implements Initializable {

    public VBox song_liked;
    public HBox playlist_liked;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        performInitialization();
    }

    private void performInitialization() {

        //Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);

        // Get Data Current User
        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;

        //ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item().clear();

        getDataLikedPlaylist(userId);
        getLikedSongFromData(userId);
    }

    public void getDataLikedPlaylist(Integer userId) {
        try (Connection connection = ConnectDB.getConnection()) {
            String query = "SELECT PL.* FROM playlist PL JOIN likeplaylist LPL ON LPL.playlistId = PL.playlistId AND LPL.userId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<PlaylistItem> listPlaylist = processResultSet(resultSet);
                    updateUI(listPlaylist);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(List<PlaylistItem> listPlaylist) {
        //playlist_liked.getChildren().clear();
        for (PlaylistItem playlistItem : listPlaylist) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/PlaylistView/PlaylistItem.fxml"));

            try {
                AnchorPane hBox = fxmlLoader.load();
                PlaylistItemController cic = fxmlLoader.getController();
                cic.setData(playlistItem);
                playlist_liked.getChildren().add(hBox);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private List<PlaylistItem> processResultSet(ResultSet resultSet) throws SQLException {
        List<PlaylistItem> playlistItems = new ArrayList<>();

        while (resultSet.next()) {
            int playlistId = resultSet.getInt("playlistId");
            String thumbnailPlaylist = resultSet.getString("thumbnailPlaylist");
            String category = resultSet.getString("category");
            String namePlaylist = resultSet.getString("namePlaylist");
            int authorId = resultSet.getInt("authorId");
            int quantitySong = resultSet.getInt("quantitySong");
            String description = resultSet.getString("description");

            PlaylistItem playlistItem = new PlaylistItem(playlistId, thumbnailPlaylist, category,
                    namePlaylist, authorId, quantitySong, description);
            playlistItems.add(playlistItem);
        }

        return playlistItems;
    }

    public void getLikedSongFromData(Integer userId) {
        try{
            Connection connection = ConnectDB.getConnection();
//            String query = "SELECT * FROM song S JOIN likesong LS ON LS.songId = S.songId AND LS.userId = ?";
            String query = "SELECT * FROM song S " +
                    "JOIN likesong LS ON LS.songId = S.songId AND LS.userId = ? " +
                    "JOIN user ON user.userId = S.authorId";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ListSongPlaying.SongListGlobal.songList.removeListSong();
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
                        ListSongPlaying.SongListGlobal.songList.addSong(song);
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItem.fxml"));
                        try {
                            AnchorPane hBox = fxmlLoader.load();
                            SongItemController cic = fxmlLoader.getController();
                            cic.setData(song);
                            song_liked.getChildren().add(hBox);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reload Page
    public void reloadCurrentPage(ActionEvent event) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.COLLECTION);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Client/Collection.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }
}