package com.example.app.Controller.Admin;

import com.example.app.Controller.Client.BottomClientController;
import com.example.app.Models.Model;
import com.example.app.Views.AdminMenuOptions;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController extends BottomClientController implements Initializable {
    public Button manage_user;
    public Button manage_song;
    public Button library_page_btn;
    public Button check_song;
    public Button abum;
    public Button playlist;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manage_user.setOnAction(event -> manageUserView());
        manage_song.setOnAction(event -> manageSongView());
        abum.setOnAction(event -> manage_album(event));
        playlist.setOnAction(event -> managePlaylistView(event));
        check_song.setOnAction(event -> checkSongView());

        logout_btn.setOnAction(this::Logout);

    }

    private void manage_album(ActionEvent event) {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.ABUM);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Admin/ManageAlbum/ManageAlbum.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }

    private void manageUserView() {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANGAGE_USER);
    }

    private void manageSongView() {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_SONG);

    }


    private void managePlaylistView(ActionEvent event) {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PLAYLIST);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
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
    private void checkSongView() {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CHECK_SONG);

    }

    private void Logout (ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);

        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("");
        stage.show();
    }
}
