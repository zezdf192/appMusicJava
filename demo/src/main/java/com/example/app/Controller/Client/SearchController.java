package com.example.app.Controller.Client;

import com.example.app.ConnectDB.ConnectDB;

import com.example.app.Controller.Client.Song.SongItemController;
import com.example.app.Models.Song.Song;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;



public class SearchController implements Initializable {

    public TextField text_search;
    public Button search_btn;
    public VBox vbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_search.setText("");
        vbox.getChildren().clear();

        PauseTransition pauseTransition;
        pauseTransition = new PauseTransition(Duration.millis(500));
        pauseTransition.setOnFinished(event -> {
            // Hàm được gọi khi độ trễ kết thúc
            searchDB();
        });

        text_search.setOnKeyPressed((KeyEvent event) -> {
            pauseTransition.playFromStart();
        });
    }

    public void searchDB() {
        vbox.getChildren().clear();
        if(text_search.getText().isEmpty()) {
            return;
        }
        try{
            Connection connection = ConnectDB.getConnection();
            String text = text_search.getText();
            String query = "select * from song join user on song.authorId = user.userId AND song.privacy = 'P1' and nameSong LIKE '%" + text + "%' ";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println(resultSet);
            while (resultSet.next()) {
                int songId = resultSet.getInt("songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                String dateCreated =  resultSet.getString("dateCreated");
                String totalLike =  resultSet.getString("totalLike");
                String pathSong =  resultSet.getString("pathSong");
                String pathImg =  resultSet.getString("pathImg");
                String kindOfSong =  resultSet.getString("kindOfSong");
                Song song = new Song(songId, nameSong, nameAuthor, dateCreated, totalLike, pathSong, pathImg, kindOfSong);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItem.fxml"));
                try {
                    AnchorPane hBox = fxmlLoader.load();
                    SongItemController cic = fxmlLoader.getController();
                    cic.setData(song);
                    vbox.getChildren().add(hBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //text_search.setText("");
            //vbox.getChildren().clear();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}