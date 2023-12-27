package com.example.app.Controller.Client.PlaylistUserCreate;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Client.Playlist.PlaylistItemController;
import com.example.app.Controller.Data;
import com.example.app.Models.Model;
import com.example.app.Models.Playlist.CurrentPlaylist;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PlaylistItemUserCreateController extends PlaylistItemController {
    public ImageView img;
    public Label namePlaylist;
    public Text des_Playlist;

    //public Image url_Img;
    public int playlistId;
    public AnchorPane container;

    String thumbnailPlaylist;
    String category;
    int authorId;
    int quantitySong;
    String description;

    public void setData (PlaylistItem playlistItem) {

        // Tải ảnh từ đường dẫn
        Image image = new Image(playlistItem.getThumbnailPlaylist());

        // Đặt ảnh cho đối tượng ImageView
        img.setImage(image);

        namePlaylist.setText(playlistItem.getNamePlaylist());
        des_Playlist.setText(playlistItem.getDescription());

        //Update info playlist
        playlistId = playlistItem.getPlaylistId();
        thumbnailPlaylist = playlistItem.getThumbnailPlaylist();
        category = playlistItem.getCategory();
        authorId = playlistItem.getAuthorId();
        quantitySong = playlistItem.getQuantitySong();
        description = playlistItem.getDescription();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ListSongPlaying.SongListGlobal.songList.removeListSong();

                CurrentPlaylist currentPlaylist = new CurrentPlaylist(playlistId, thumbnailPlaylist, category, namePlaylist.getText(),  authorId,  quantitySong,  description);
                Data.getDataGLobal.dataGlobal.setCurrentPlaylist(currentPlaylist);

                try{
                    Connection connection = ConnectDB.getConnection();
                    String query = "select * from song join playlist join user on song.playListId = playlist.playlistId and user.userId = song.authorId and playlist.playlistId =" + playlistId;
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

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
                        ListSongPlaying.SongListGlobal.songList.addSong(song);
                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }

                //Đặt lại Playlist
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/Fxml/Client/DetailPlaylistUser.fxml"));

                Parent viewBottomClient;
                try {
                    viewBottomClient = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the new BottomClient as the bottom of the o
                Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DETAIL);
                borderPane.setCenter(viewBottomClient);
            }
        });
    }
}
