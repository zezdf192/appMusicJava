package com.example.app.Controller.Admin.ManageSong;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Admin.ItemUser;
import com.example.app.Models.Model;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import static com.example.app.Controller.Client.BottomClientController.mediaPlayer;

public class ItemSongController implements Initializable {
    public Label category;
    public Label name;
    public Label dateCreated;
    public Label totalLike;
    public Label privacy;
    public Button update_btn;
    public Button remove_btn;
    public HBox hbox;
    private String pathSong;
    private String pathImg;
    private String nameAuthor;
    private int id;
    private int authorId;
    private int albumId;
    private int playlistId;
    private String desAdmin;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMusic();
        remove_btn.setOnAction(event -> removeUser(event));
        update_btn.setOnAction(event -> updateSong());
    }

    public void updateSong() {
        ItemSong song = new ItemSong(id, name.getText(), nameAuthor, authorId, albumId,
                dateCreated.getText(), Integer.valueOf(totalLike.getText()), pathSong
                , pathImg, category.getText(), playlistId, privacy.getText(), desAdmin );
        Data.getDataGLobal.dataGlobal.setCurrentEditSong(song);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageSong/UpdateSong.fxml"));
            Stage createUserStage = new Stage();
            createStage(loader, createUserStage);
            createUserStage.setOnHiding(event -> {
                updateData();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateData() {
        ItemSong song = Data.getDataGLobal.dataGlobal.getCurrentEditSong();
        name.setText(song.getNameSong());

    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Update new song");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeUser(ActionEvent event) {
        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "DELETE FROM song WHERE songId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.valueOf(id));
                int rowsAffected = preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Admin/ManageSong/ManageSong.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);

    }

    public void setData(ItemSong song) {
        name.setText(song.getNameSong());
        category.setText(song.getKindOfSong());
        dateCreated.setText(song.getDateCreated());
        totalLike.setText(String.valueOf(song.getTotalLike()));
        privacy.setText(song.getPrivacy());
        pathSong = song.getPathSong();
        pathImg = song.getPathImg();
        id = song.getSongId();
        nameAuthor = song.getNameAuthor();
        authorId = song.getAuthorId();
        albumId = song.getAbumId();
        playlistId = song.getPlaylistId();
        desAdmin = song.getDesAdmin();
    }

    public void chooseMusic() {

        hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //SongItemHome song = new SongItemHome(nameSong.getText(), nameAuthor.getText(), abum.getText(), dateCreated.getText(), totalTime.getText(), pathSong);
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);
                Song song = new Song(1,
                        name.getText(), nameAuthor,dateCreated.getText(),
                        totalLike.getText(),
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

}
