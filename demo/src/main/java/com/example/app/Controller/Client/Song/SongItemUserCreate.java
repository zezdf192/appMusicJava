package com.example.app.Controller.Client.Song;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Admin.ManageAlbum.ItemSongSearchController;
import com.example.app.Controller.Data;
import com.example.app.Models.Song.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class SongItemUserCreate extends ItemSongSearchController implements Initializable{
    public HBox songContainer;
    public Label nameSong;
    public ImageView img;
    public Label author;
    public Button add_btn;

    private int songId;

    private int playlistId;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_btn.setOnAction(event -> addSongToPlaylist(event));
    }

    public void setData(Song song) {
        // Tải ảnh từ đường dẫn
        Image image = new Image(song.getPathImg());
        System.out.println(image);
        img.setImage(image);
        nameSong.setText(song.getNameSong());
        songId = song.getSongId();
        playlistId = Data.getDataGLobal.dataGlobal.getCurrentPlaylist().getPlaylistId();
    }

    private void addSongToPlaylist(ActionEvent event) {

        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "INSERT INTO album_song_user (playListId, songId, role) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, playlistId);
                preparedStatement.setInt(2, songId);
                preparedStatement.setString(3, "R2");
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("TC");
                } else {
                    System.out.println("TB");
                }
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

            // Load a new instance of BottomClient
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Fxml/Client/DetailPlaylistUser.fxml"));
            Parent viewBottomClient;
            try {
                viewBottomClient = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            borderPane.setCenter(viewBottomClient);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
