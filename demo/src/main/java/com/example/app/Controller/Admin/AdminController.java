package com.example.app.Controller.Admin;

import com.example.app.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    public BorderPane admin_parent;

    public BorderPane getAdmin_parent() {
        return admin_parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewAdminFactory().getAdminSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case MANGAGE_USER:
                    admin_parent.setCenter(Model.getInstance().getViewAdminFactory().getManageUserView());
                    break;
                case MANAGE_SONG:
                    admin_parent.setCenter(Model.getInstance().getViewAdminFactory().getManageUserSong());
                    break;
                case ABUM:
                    admin_parent.setCenter(Model.getInstance().getViewAdminFactory().getManageAlbum());
                    break;
                case PLAYLIST:
                    admin_parent.setCenter(Model.getInstance().getViewAdminFactory().getManagePlaylist());
                    break;
                case CHECK_SONG:
                    admin_parent.setCenter(Model.getInstance().getViewAdminFactory().getCheckSong());
                    break;
                default:

                    break;
            }
        });
    }
}
