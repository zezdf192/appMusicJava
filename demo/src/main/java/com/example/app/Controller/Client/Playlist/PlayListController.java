package com.example.app.Controller.Client.Playlist;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Playlist.ListPlayList;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class PlayListController implements Initializable {

    public HBox hbox_playlist;
    public Button display_createPlaylist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = user.getUserId();

        getDataPlaylist(userId, new ActionEvent());

        display_createPlaylist.setOnAction(actionEvent -> {
            handleCreateNewPlaylist(userId);
//            ListPlayList.ListPlayListGlobal.songList.removeSongList();
//            ListPlayList.ListPlaylistCurrentUser.songList.removeSongList();

        });
    }

//    public void createPlaylist(Integer userId, ActionEvent event) throws SQLException {
//
//        Connection connection = ConnectDB.getConnection();
//        String sql = "INSERT INTO playlist (thumbnailPlaylist, category, namePlaylist, authorId, quantitySong, description) VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, "https://res.cloudinary.com/djfpcyyfe/image/upload/v1701951012/mvjnijmcsnvheco0bhzw.png");
//            preparedStatement.setString(2, "");
//            preparedStatement.setString(3, name_playlist.getText());
//            preparedStatement.setInt(4, userId);
//            preparedStatement.setInt(5, 0);
//            preparedStatement.setString(6, "Playlist của bạn!");
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Playlist created successfully.");
//                showAlert("Thành công", "Đã thêm playlist vào danh sách!", Alert.AlertType.INFORMATION);
//                name_playlist.setText("");
//            } else {
//                System.out.println("Failed to create playlist.");
//            }
//        }
//
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
//
//        // Load a new instance of BottomClient
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/Fxml/Client/PlayList.fxml"));
//        Parent viewBottomClient;
//        try {
//            viewBottomClient = loader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        borderPane.setCenter(viewBottomClient);
//    }

    public void getDataPlaylist (Integer userId, ActionEvent event) {
//        ListPlayList.ListPlayListGlobal.songList.removeSongList();

        ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item().clear();
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from playlist where authorId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int playlistId = resultSet.getInt("playlistId");
                    String thumbnailPlaylist = resultSet.getString("thumbnailPlaylist");
                    String category =  resultSet.getString("category");
                    String namePlaylist =  resultSet.getString("namePlaylist");
                    int authorId =  resultSet.getInt("authorId");
                    int quantitySong =  resultSet.getInt("quantitySong");
                    String description =  resultSet.getString("description");

                    PlaylistItem playlistItem = new PlaylistItem(playlistId, thumbnailPlaylist, category,
                            namePlaylist, authorId, quantitySong, description);
                    ListPlayList.ListPlaylistCurrentUser.songList.addListPlayList(playlistItem);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            List<PlaylistItem> listPlaylist = ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item();
            System.out.println(listPlaylist.size());
            for (int i = 0; i < listPlaylist.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/PlaylistView/PlaylistItemUserCreate.fxml"));

                try {
                    AnchorPane hBox = fxmlLoader.load();
                    PlaylistItemController cic = fxmlLoader.getController();
                    cic.setData(listPlaylist.get(i));
                    hbox_playlist.getChildren().add(hBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataPlaylist1Song (Integer userId, ActionEvent event) {
//        ListPlayList.ListPlayListGlobal.songList.removeSongList();

//        ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item().clear();
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from playlist where authorId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int playlistId = resultSet.getInt("playlistId");
                    String thumbnailPlaylist = resultSet.getString("thumbnailPlaylist");
                    String category =  resultSet.getString("category");
                    String namePlaylist =  resultSet.getString("namePlaylist");
                    int authorId =  resultSet.getInt("authorId");
                    int quantitySong =  resultSet.getInt("quantitySong");
                    String description =  resultSet.getString("description");

                    PlaylistItem playlistItem = new PlaylistItem(playlistId, thumbnailPlaylist, category,
                            namePlaylist, authorId, quantitySong, description);
                    ListPlayList.ListPlaylistCurrentUser.songList.addListPlayList(playlistItem);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            List<PlaylistItem> listPlaylist = ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/PlaylistView/PlaylistItemUserCreate.fxml"));

                try {
                    AnchorPane hBox = fxmlLoader.load();
                    PlaylistItemController cic = fxmlLoader.getController();
                    cic.setData(listPlaylist.get(0));
                    hbox_playlist.getChildren().add(hBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCreateNewPlaylist(Integer userId) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/PlaylistView/CreatePlaylist.fxml"));
        Stage createUserStage = new Stage();
        createStage(loader, createUserStage);

        createUserStage.setOnHiding(event -> {
            ListPlayList.ListPlaylistCurrentUser.songList.getListPlaylist_Item().clear();
            getDataPlaylist1Song(userId, new ActionEvent());

        });
    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Create new playlist");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage (ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        // Load a new instance of BottomClient
        FXMLLoader loaderPlaylist = new FXMLLoader();
        loaderPlaylist.setLocation(getClass().getResource("/Fxml/Client/PlayList.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loaderPlaylist.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);
    }

}