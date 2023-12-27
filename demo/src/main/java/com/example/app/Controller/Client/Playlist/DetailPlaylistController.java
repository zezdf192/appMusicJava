package com.example.app.Controller.Client.Playlist;

import com.example.app.ConnectDB.ConnectDB;

import com.example.app.Controller.Client.Song.SongItemController;
import com.example.app.Controller.Data;
import com.example.app.Models.Playlist.CurrentPlaylist;

import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DetailPlaylistController implements Initializable {
    public VBox vbox;
    public ScrollPane scroll_songitem;
    public VBox vbox_layout;
    public FontAwesomeIconView like_Playlist;
    public Label des_likePlaylist;

    public Label namePlaylist;
    public ImageView img;
    public Label category;


    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CurrentPlaylist currentPlaylist = Data.getDataGLobal.dataGlobal.getCurrentPlaylist();

        namePlaylist.setText(currentPlaylist.getNamePlaylist());
        category.setText(currentPlaylist.getCategory());
        Image image = new Image(currentPlaylist.getThumbnailPlaylist());
        img.setImage(image);

        Integer playlistId = currentPlaylist.getPlaylistId();

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;

        List<Song> listsongs = ListSongPlaying.SongListGlobal.songList.getListSongs();

        for (int i = 0; i < listsongs.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/SongItem.fxml"));
            try {
                AnchorPane hBox = fxmlLoader.load();
                SongItemController cic = fxmlLoader.getController();
                cic.setData(listsongs.get(i));
                vbox_layout.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        checkIfPlaylistIsLiked(playlistId, userId);

        like_Playlist.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                if (userId != null) {
                    handleLikePlaylistClicked(event, playlistId, userId);
                } else {
                    // Handle the case where userId is null (optional)
                    System.out.println("UserId not found!");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception as needed (log or show user-friendly message)
            }
        });
    }

    private void handleLikePlaylistClicked(MouseEvent event,Integer playlistId, Integer userId) throws SQLException {
        Color newColor = Color.web("#7230e4");
        if (like_Playlist.getFill().equals(Color.WHITE)) {
            like_Playlist.setFill(newColor);
            des_likePlaylist.setText("Đã thêm vào bộ sưu tập!");

            try (Connection connection = ConnectDB.getConnection()) {
                String sql = "INSERT INTO likeplaylist (playlistId, userId) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, playlistId);
                    preparedStatement.setInt(2, userId);

                    int rowsAffected = preparedStatement.executeUpdate();

                    // Kiểm tra xem truy vấn đã thành công hay không
                    if (rowsAffected > 0) {
                        System.out.println("Like playlist successfully added.");
                    } else {
                        System.out.println("Failed to like playlist.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception as needed (log or show user-friendly message)
            }
        } else {
            like_Playlist.setFill(Color.WHITE);
            des_likePlaylist.setText("Bạn có thích Playlist này?");

            try (Connection connection = ConnectDB.getConnection()) {
                String deleteLikePLaylist = "DELETE from likeplaylist where playlistId = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteLikePLaylist)) {
                    preparedStatement.setInt(1, playlistId);
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Kiểm tra xem truy vấn đã thành công hay không
                    if (rowsAffected > 0) {
                        System.out.println("Like playlist successfully added.");
                    } else {
                        System.out.println("Failed to like playlist.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Additional actions after clicking the heart icon can be added here
    }
    private void checkIfPlaylistIsLiked(Integer playlistId, Integer userId) {
        Color newColor = Color.web("#7230e4");
        try (Connection connection = ConnectDB.getConnection()) {
            String checkLikeSong = "SELECT * FROM likeplaylist WHERE playlistId = ? AND userId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(checkLikeSong)) {
                preparedStatement.setInt(1, playlistId);
                preparedStatement.setInt(2, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Bài hát đã được thích, đặt màu cho hình trái tim
                        like_Playlist.setFill(newColor);
                        des_likePlaylist.setText("Đã thêm vào bộ sưu tập!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log or show user-friendly message)
        }
    }
}



