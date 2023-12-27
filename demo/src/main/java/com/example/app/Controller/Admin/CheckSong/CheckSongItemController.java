package com.example.app.Controller.Admin.CheckSong;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Model;
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
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import static com.example.app.Controller.Client.BottomClientController.mediaPlayer;

public class CheckSongItemController implements Initializable {
    public Label category;
    public Label name;
    public Label dateCreated;
    public Label totalLike;
    public Label privacy;
    public HBox hbox;

    private String pathSong;
    private String pathImg;
    private String nameAuthor;
    private int id;
    private int authorId;
    private int albumId;
    private int playlistId;
    private String desAdmin;
    public Button agree_btn;
    public Button refuse_btn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMusic();
        agree_btn.setOnAction(event -> handleAgreeSong(event));
        refuse_btn.setOnAction(event -> hanldeRefuse(event));
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

    private void handleAgreeSong(ActionEvent event) {
        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "UPDATE song SET privacy = 'P1', desAdmin = ? WHERE songId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "Thành công");
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

            // Load a new instance of BottomClient
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Fxml/Admin/CheckSong/CheckSong.fxml"));
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

    private void hanldeRefuse(ActionEvent ev) {
        ItemSong song = new ItemSong(id, name.getText(), nameAuthor, authorId, albumId,
                dateCreated.getText(), Integer.valueOf(totalLike.getText()), pathSong
                , pathImg, category.getText(), playlistId, privacy.getText(), desAdmin );
        Data.getDataGLobal.dataGlobal.setCurrentEditSong(song);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/CheckSong/UpdateCheckSong.fxml"));
            Stage createUserStage = new Stage();
            createStage(loader, createUserStage);
            createUserStage.setOnHiding(event -> {
                updateData(ev);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void updateData(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Admin/CheckSong/CheckSong.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Create new user");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
