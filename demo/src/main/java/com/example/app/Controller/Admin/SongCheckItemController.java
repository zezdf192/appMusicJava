package com.example.app.Controller.Admin;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;

import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.app.Controller.Client.BottomClientController.mediaPlayer;

public class SongCheckItemController implements Initializable {
    private int songId;
    public Label nameSong;
    public Label nameAuthor;
    public Label abum;
    public Label dateCreated;
    public Button accept_btn;
    public Button refuse_btn;
    public TextField mess;
    public HBox songContainer;

    public String pathSong;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMusic();
        refuse_btn.setOnAction(event -> refuseSong(event));
    }

    public void setData(Song song) {
        songId = song.getSongId();
        nameSong.setText(song.getNameSong());
        nameAuthor.setText(song.getNameAuthor());
        abum.setText(song.getKindOfSong());
        dateCreated.setText(song.getDateCreated());
        pathSong = song.getPathSong();
    }



    private void refuseSong(ActionEvent event)  {

        Connection connection = null;
        try {
            connection = ConnectDB.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String query = "UPDATE song SET privacy = 'P2', descriptionAdmin = ? WHERE songId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Assuming pathSong is a String, replace setString with the appropriate method if it's a different data type
            preparedStatement.setString(1, mess.getText());
            preparedStatement.setInt(2, songId);

            int rowsAffected = preparedStatement.executeUpdate();
            //System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

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


    public void chooseMusic() {

        songContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //SongItemHome song = new SongItemHome(nameSong.getText(), nameAuthor.getText(), abum.getText(), dateCreated.getText(), totalTime.getText(), pathSong);

                Song song = new Song(1,
                        nameSong.getText(),nameAuthor.getText(),dateCreated.getText(),
                        "1",
                        pathSong,"",""
                );
                Data.getDataGLobal.dataGlobal.setCurrentSong(song);

                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                }



                //ListSongPlaying.SongListGlobal.songList.addSong(song);
                //stopCurrentSong();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                // Remove the existing BottomClient
//                borderPane.setBottom(null);


                // Load a new instance of BottomClient
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Fxml/Client/BottomClient.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int count = ListSongPlaying.SongListGlobal.songList.getCount();

//                for (int i = 0; i < count ; i++) {
//                    if(ListSongPlaying.SongListGlobal.songList.getSong(i).getPathSong().equals(Data.getDataGLobal.dataGlobal.getSong().getPathSong())) {
//                        ListSongPlaying.SongListGlobal.songList.setSttPlaySong(i);
//                        break;
//                    }
//                }

                // Set the new BottomClient as the bottom of the BorderPane
                borderPane.setBottom(viewBottomClient);
            }
        });

    }
}
