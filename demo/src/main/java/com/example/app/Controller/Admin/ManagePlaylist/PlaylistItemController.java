package com.example.app.Controller.Admin.ManagePlaylist;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemAlbum;
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

public class PlaylistItemController implements Initializable {
    private int id;
    private int authorId;
    private int quantitySong;
    private String thumbnail;
    public Label name;
    public Label description;
    public Label totalSong;
    public Button update_btn;
    public Button remove_btn;
    public HBox hbox;

    public void setData(ItemAlbum album) {
        id = album.getId();
        quantitySong = album.getQuantitySong();
        authorId = album.getAuthorId();
        thumbnail = album.getThumbnail();
        name.setText(album.getName());
        description.setText(album.getDescription());
        totalSong.setText(String.valueOf(album.getQuantitySong()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update_btn.setOnAction(event -> updateAlbum());
        remove_btn.setOnAction(event -> removeAlbum(event));
        chooseAlbum();
    }


    public void updateAlbum() {

        ItemAlbum album = new ItemAlbum(id, thumbnail,  name.getText(), authorId, quantitySong, description.getText());
        Data.getDataGLobal.dataGlobal.setCurrentEditAlbum(album);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManagePlaylist/UpdatePlaylist.fxml"));
            Stage createUserStage = new Stage();
            createStage(loader, createUserStage);
            createUserStage.setOnHiding(event -> {
                updateData();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Update new album");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateData() {
        ItemAlbum album = Data.getDataGLobal.dataGlobal.getCurrentEditAlbum();
        name.setText(album.getName());
        description.setText(album.getDescription());
    }

    private void removeAlbum(ActionEvent event) {
        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "DELETE FROM playlist WHERE playlistId = ?";

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
        loader.setLocation(getClass().getResource("/Fxml/Admin/ManagePlaylist/ManagePlaylist.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);

    }

    public void chooseAlbum() {
        hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ItemAlbum album = new ItemAlbum(id, thumbnail,  name.getText(), authorId, quantitySong, description.getText());
                Data.getDataGLobal.dataGlobal.setCurrentEditAlbum(album);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/Fxml/Admin/ManagePlaylist/DefaultPlaylist.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the new BottomClient as the bottom of the o
                borderPane.setCenter(viewBottomClient);

            }
        });

    }
}
