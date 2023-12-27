package com.example.app.Controller.Client;

import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemAlbum;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Admin.ItemUser;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.example.app.Models.Model;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.app.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;
public class ClientMenuController extends BottomClientController implements Initializable {
    public Button home_btn;

    Scene scene = null;

    public Button search_btn;
    public Button collection_btn;
    public Button mymusic_btn;
    public Button playlist_btn;
    public Button library_page_btn;
    public Button add_playlist_page_btn;
    public Button logout_btn;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    public void addListener() {
        home_btn.setOnAction(this::onHome);
        search_btn.setOnAction(this::onSearch);
        collection_btn.setOnAction(this::onCollection);
        mymusic_btn.setOnAction(this::onMyMusic);
        playlist_btn.setOnAction(this::onPlaylist);
        logout_btn.setOnAction(this::Logout);
    }

    private void onHome(ActionEvent event) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.HOME);
    }


    private void onSearch(ActionEvent event) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Client/Search.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }

    private void onCollection(ActionEvent event) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);
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

    private void onPlaylist(ActionEvent event) {

        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PLAY_LIST);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Client/PlayList.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }

    private void onMyMusic(ActionEvent event) {

        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.MY_MUSIC);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Client/MyMusic.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);

    }

    private void loadView(ActionEvent event, String resourcePath) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent viewBottomClient = loader.load();
            borderPane.setCenter(viewBottomClient);
        } catch (IOException e) {
            // Handle the exception gracefully, e.g., log it or display an alert
            e.printStackTrace();
        }
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