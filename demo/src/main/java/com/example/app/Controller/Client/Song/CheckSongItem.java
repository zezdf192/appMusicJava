package com.example.app.Controller.Client.Song;

import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Model;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CheckSongItem extends SongItemController implements Initializable {
    public Label nameSong;
    public Label nameAuthor;
    public ImageView img;
    public Label abum;
    public Label dateCreated;

    public String pathImg;
    public Integer songId;
    public String pathSong;
    public Label status;
    public AnchorPane box;
    public HBox songContainer;

    public void setData(ItemSong song) {
        // Tải ảnh từ đường dẫn
        Image image = new Image(song.getPathImg());

        pathImg = song.getPathImg();
        songId = song.getSongId();
        // Đặt ảnh cho đối tượng ImageView
        img.setImage(image);
        nameSong.setText(song.getNameSong());
        nameAuthor.setText(song.getNameAuthor());
        abum.setText(song.getKindOfSong());
        dateCreated.setText(song.getDateCreated());
//        totalTime.setText(song.getTotalLike());
        pathSong = song.getPathSong();
        status.setText(song.getDesAdmin());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMusic();
    }

    public void chooseMusic() {
        songContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //SongItemHome song = new SongItemHome(nameSong.getText(), nameAuthor.getText(), abum.getText(), dateCreated.getText(), totalTime.getText(), pathSong);
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.X);
                Song song = new Song(songId,
                        nameSong.getText(),nameAuthor.getText(),dateCreated.getText(),
                        "0",
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

                // Set the new BottomClient as the bottom of the BorderPane
                borderPane.setBottom(viewBottomClient);
            }
        });

    }

    public Label getNameSong() {
        return nameSong;
    }
}
