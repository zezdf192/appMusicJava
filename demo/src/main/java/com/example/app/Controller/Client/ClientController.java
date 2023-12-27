package com.example.app.Controller.Client;

import com.example.app.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ClientController implements Initializable {
    public BorderPane client_parent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case SEARCH:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
                    break;
                case COLLECTION:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getCollectionView());
                    break;
                case MY_MUSIC:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getMyMusicView());
                    break;
                case PLAY_LIST:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getPlaylistView());
                    break;
                case BOTTOM_VIEW:
                    client_parent.setBottom(Model.getInstance().getViewFactory().getBottomView());
                    break;
                case DIV:
                    client_parent.setBottom(Model.getInstance().getViewFactory().getDivView());
                    break;
                case HOME:
                    client_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
                    break;
                default:

                    break;
            }
        });
    }
}