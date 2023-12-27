package com.example.app.Controller.Admin.ManageSong;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Models.Admin.ItemSong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateNewSongController implements Initializable {


    public TextField nameSong;
    public Button upload_thumnail;
    public Button upload_song;
    public ComboBox category;
    public Button upload;
    public ComboBox abum;
    public ComboBox playlist;
    //public ComboBox privacy;
    public ComboBox author;

    private String urlSong;
    private String urlThumbnail;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        upload_song.setOnAction(actionEvent -> urlSong = uploadSongs(upload_song));
        upload_thumnail.setOnAction(actionEvent -> urlThumbnail = uploadThumbnail(upload_thumnail));

        ObservableList<String> genres = FXCollections.observableArrayList(
                "Pop", "Rock", "Hip Hop", "R&B", "Country", "Jazz", "Classical"
        );

        setComboBoxArtist();
        setComboBoxAbum();
        setComboBoxPlaylist();

        category.setItems(genres);
        category.setValue(genres.get(0));

        upload.setOnAction(actionEvent -> create_Song());
    }

    private void setComboBoxPlaylist() {
        PlaylistDB non = new PlaylistDB("Không playlist", -1);
        ObservableList<PlaylistDB> playlists = FXCollections.observableArrayList(non);

        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from playlist";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("namePlaylist");
                int id = resultSet.getInt("playlistId");
                PlaylistDB a = new PlaylistDB(name, id);
                playlists.add(a);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        playlist.setItems(playlists);
        playlist.setValue(playlists.get(0));

        playlist.setConverter(new StringConverter<PlaylistDB>() {
            @Override
            public String toString(PlaylistDB object) {
                return object.getName();
            }

            @Override
            public PlaylistDB fromString(String string) {
                return null;
            }
        });
    }

    private  void setComboBoxAbum() {
        AbumDB non = new AbumDB("Không album", -1);
        ObservableList<AbumDB> abums = FXCollections.observableArrayList(non);

        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from album";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("nameAlbum");
                int id = resultSet.getInt("albumId");
                AbumDB a = new AbumDB(name, id);
                abums.add(a);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        abum.setItems(abums);
        abum.setValue(abums.get(0));

        abum.setConverter(new StringConverter<AbumDB>() {
            @Override
            public String toString(AbumDB object) {
                return object.getName();
            }

            @Override
            public AbumDB fromString(String string) {
                return null;
            }
        });
    }
    private void setComboBoxArtist() {
        ObservableList<AuthorDB> artist = FXCollections.observableArrayList();

        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from user where role = 'R3'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nameAuthor = resultSet.getString("nameUser");
                int id = resultSet.getInt("userId");
                AuthorDB a = new AuthorDB(nameAuthor, id);
                artist.add(a);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        author.setItems(artist);
        author.setValue(artist.get(0));

        author.setConverter(new StringConverter<AuthorDB>() {
            @Override
            public String toString(AuthorDB object) {
                return object.getName();
            }

            @Override
            public AuthorDB fromString(String string) {
                return null;
            }
        });
    }

    private String uploadSongs(Button buttonUpdate) {
        return uploadFile("Select a Song File", buttonUpdate);
    }

    private String uploadThumbnail(Button buttonUpdate) {
        return uploadFile("Select a Thumbnail File", buttonUpdate);
    }

    private String uploadFile(String title, Button buttonUpdate) {
        // Configuring Cloudinary
        String CLOUDINARY_URL = "cloudinary://462886354475568:7IyZ1fMXe--ZoDhLd9hr4zlkGOQ@djfpcyyfe";
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;

        // Creating a FileChooser to select the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        File file = new File(selectedFile.toString());
        String fileName = file.getName();
        fileName = fileName.length() > 15 ? fileName.substring(0, 15) : fileName;
        fileName = fileName + "...";

        if (selectedFile != null) {
            try {
                // Uploading the file to Cloudinary
                Map<String, Object> uploadParams = new HashMap<>();
                uploadParams.put("resource_type", "auto");

                Map<?, ?> uploadResult = cloudinary.uploader().upload(selectedFile, uploadParams);

                // Accessing the public URL of the uploaded file
                String publicUrl = (String) uploadResult.get("secure_url");

                buttonUpdate.setText(fileName);

                // You can now use 'publicUrl' for further processing or display

                System.out.println("File uploaded successfully. Public URL: " + publicUrl);
                return publicUrl;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error uploading the file.");
            }
        }
        return null;
    }

    private void create_Song() {
        AuthorDB at = (AuthorDB) author.getValue();
        AbumDB ab = (AbumDB) abum.getValue();
        PlaylistDB pl = (PlaylistDB) playlist.getValue();

        try (Connection connection = ConnectDB.getConnection()) {
            String songName = nameSong.getText();
            String pathSong = urlSong;
            String thumbnail = urlThumbnail;
            String ownerArtist = String.valueOf(author.getValue()); // You might want to get this dynamically based on user input or authentication
            String kindOfSong = String.valueOf(category.getValue());
            LocalDate currentDate = LocalDate.now();

            // Định dạng ngày tháng theo "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);
            Date sqlDate = Date.valueOf(currentDate);

            if (songName.isEmpty() || pathSong.isEmpty() || thumbnail.isEmpty() || kindOfSong.isEmpty()) {
                showAlert("Lỗi", "Thông tin bài nhạc bị khiếm khuyết!", Alert.AlertType.ERROR);
            } else {

                String sql = "INSERT INTO song (nameSong, authorId, albumId, dateCreated, pathSong, kindOfSong, playListId, totalLike, pathImg, privacy, desAdmin, dateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, songName);
                    preparedStatement.setInt(2,at.getId());
                    preparedStatement.setInt(3, ab.getId());
                    preparedStatement.setString(4, formattedDate);
                    preparedStatement.setString(5, pathSong);
                    preparedStatement.setString(6, kindOfSong);
                    preparedStatement.setInt(7,pl.getId());
                    preparedStatement.setInt(8, 0);
                    preparedStatement.setString(9, thumbnail);
                    preparedStatement.setString(10, "P1");
                    preparedStatement.setString(11, "");
                    preparedStatement.setDate(12, sqlDate);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {


                    } else {
                        showAlert("Lỗi", "Không thể thêm bài hát!", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi không xác định!", Alert.AlertType.ERROR);
        }
        // Change quantity song in playlist or album
        updateAlbum(ab.getId());
        updatePlaylist(pl.getId());


        Stage stage = (Stage) upload.getScene().getWindow();
        stage.close();
    }

    private void updateAlbum (int albumId) {
        try (Connection connection = ConnectDB.getConnection()) {
            String selectSql = "SELECT quantitySong FROM album WHERE albumId = ?";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, albumId);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int currentQuantity = resultSet.getInt("quantitySong");

                        // Increment the quantity by 1
                        int newQuantity = currentQuantity + 1;

                        String updateSql = "UPDATE album SET quantitySong = ? WHERE albumId = ?";

                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, albumId);

                            int rowsAffected = updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updatePlaylist (int albumId) {
        try (Connection connection = ConnectDB.getConnection()) {
            String selectSql = "SELECT quantitySong FROM playlist WHERE playlistId = ?";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, albumId);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int currentQuantity = resultSet.getInt("quantitySong");

                        // Increment the quantity by 1
                        int newQuantity = currentQuantity + 1;

                        String updateSql = "UPDATE playlist SET quantitySong = ? WHERE playlistId = ?";

                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, albumId);

                            int rowsAffected = updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
