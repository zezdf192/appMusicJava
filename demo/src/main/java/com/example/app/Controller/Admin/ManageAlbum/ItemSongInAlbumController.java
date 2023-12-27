package com.example.app.Controller.Admin.ManageAlbum;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Model;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static com.example.app.Controller.Client.BottomClientController.mediaPlayer;

public class ItemSongInAlbumController implements Initializable {
    private int songId;
    public String category;
    public String name;

    public int totalLike;
    public String privacy;
    public String update_btn;
    public Button remove_btn;
    public HBox hbox;
    public Label nameSong;
    public Label nameAuthor;
    public Label abum;
    public Label dateCreated;

    public Label remove;
    public ImageView img;
    public HBox songContainer;
    private String pathSong;
    private String pathImg;

    private int id;
    private int authorId;
    private int albumId;
    private int playlistId;
    private String desAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playMusic();
        remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try (Connection connection = ConnectDB.getConnection()) {
                    String sql = "DELETE FROM album_song_user WHERE albumId = ? and songId = ?";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setInt(1, albumId);
                        preparedStatement.setInt(2, songId);

                        int rowsAffected = preparedStatement.executeUpdate();
                    }
                    updateAlbum(albumId);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                    // Load a new instance of BottomClient
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/Fxml/Admin/ManageAlbum/DefaultAlbum.fxml"));
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
        });
    }

    private void playMusic() {
        songContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //SongItemHome song = new SongItemHome(nameSong.getText(), nameAuthor.getText(), abum.getText(), dateCreated.getText(), totalTime.getText(), pathSong);
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);
                Song song = new Song(songId,
                        nameSong.getText(),nameAuthor.getText(),dateCreated.getText(),
                        String.valueOf(totalLike),
                        pathSong,pathImg,""
                );

                Data.getDataGLobal.dataGlobal.setCurrentSong(song);
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Fxml/Client/BottomClient.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                borderPane.setBottom(viewBottomClient);
            }
        });
    }

    public void setData(ItemSong song) {
        Image image = new Image(song.getPathImg());
        img.setImage(image);
        songId = song.getSongId();
        nameSong.setText(song.getNameSong());
        category = (song.getKindOfSong());
        dateCreated.setText(song.getDateCreated());
        totalLike =(song.getTotalLike());
        privacy = (song.getPrivacy());
        pathSong = song.getPathSong();
        pathImg = song.getPathImg();
        id = song.getSongId();
        nameAuthor.setText(song.getNameAuthor());
        authorId = song.getAuthorId();
        albumId = Data.getDataGLobal.dataGlobal.getCurrentEditAlbum().getId();
        playlistId = song.getPlaylistId();
        desAdmin = song.getDesAdmin();
        abum.setText(Data.getDataGLobal.dataGlobal.getCurrentEditAlbum().getName());
    }

    private void updateAlbum (int albumId) {
        try (Connection connection = ConnectDB.getConnection()) {
            String selectSql = "SELECT quantitySong FROM album WHERE albumId = ?";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, albumId);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int currentQuantity = resultSet.getInt("quantitySong");

                        // Increment the quantity by 1
                        int newQuantity = currentQuantity + 1;

                        String updateSql = "UPDATE album SET quantitySong = ? WHERE albumId = ?";

                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, albumId);

                            int rowsAffected = updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
