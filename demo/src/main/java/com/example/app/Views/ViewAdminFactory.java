package com.example.app.Views;

import com.example.app.Controller.Admin.AdminController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewAdminFactory {
    Scene scene = null;
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane manageUserView;

    private AnchorPane manageUserSong;

    private AnchorPane manageAlbum;
    private AnchorPane managePlaylist;

    private AnchorPane checkSong;

    public ViewAdminFactory() {
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getManageUserView() {
        if (manageUserView == null) {
            try {
                manageUserView = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageUser/ManageUser.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return manageUserView;
    }

    public AnchorPane getManageUserSong() {
        if (manageUserSong == null) {
            try {
                manageUserSong = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageSong/ManageSong.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return manageUserSong;
    }

    public AnchorPane getManageAlbum() {
        if (manageAlbum == null) {
            try {
                manageAlbum = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageAlbum/ManageAlbum.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return manageAlbum;
    }

    public AnchorPane getManagePlaylist() {
        if (managePlaylist == null) {
            try {
                managePlaylist = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManagePlaylist/ManagePlaylist.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return managePlaylist;
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
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

    public void closeStage(Stage stage) {
        stage.close();
    }

    public AnchorPane getCheckSong() {
        if (checkSong == null) {
            try {
                checkSong = new FXMLLoader(getClass().getResource("/Fxml/Admin/CheckSong/CheckSong.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return checkSong;
    }



}