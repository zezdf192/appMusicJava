package com.example.app.Controller.Client;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Admin.ManagePlaylist.ItemSongInPlaylistController;
import com.example.app.Controller.Client.Song.CheckSongItem;
import com.example.app.Controller.Data;
import com.example.app.Controller.LoginController;
import com.example.app.Controller.SignupController;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;
import com.example.app.Models.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyMusicController implements Initializable {
    public Button upload_song;
    public Button upload_thumnail;
    public TextField nameSong;
    public ComboBox<String> genreComboBox;
    public Button upload;
    public ComboBox<String> privacy_comboBox;
    public VBox song_upload;

    private String urlSong;
    private String urlThumbnail;
    public List<ItemSong> listSong = new ArrayList<>();
    private User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        upload_song.setOnAction(actionEvent -> urlSong = uploadSongs(upload_song));
        upload_thumnail.setOnAction(actionEvent -> urlThumbnail = uploadThumbnail(upload_thumnail));

        ObservableList<String> genres = FXCollections.observableArrayList(
                "Pop", "Rock", "Hip Hop", "R&B", "Country", "Jazz", "Classical"
        );

        ObservableList<String> privacy = FXCollections.observableArrayList(
                "Public", "Private"
        );

        // Thêm danh sách vào ComboBox
        genreComboBox.setItems(genres);
        privacy_comboBox.setItems(privacy);

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;


        upload.setOnAction(actionEvent -> create_Song(userId, actionEvent));

        // User

        getDataSongUpload(userId);
    }

    private String uploadSongs(Button buttonUpdate) {

        return uploadFileMP3("Select a Song File", buttonUpdate);
    }

    private String uploadThumbnail(Button buttonUpdate) {
        return uploadFileImage("Select a Thumbnail File", buttonUpdate);
    }

//    private String uploadFile(String title, Button buttonUpdate) {
//        // Configuring Cloudinary
//        String CLOUDINARY_URL = "cloudinary://462886354475568:7IyZ1fMXe--ZoDhLd9hr4zlkGOQ@djfpcyyfe";
//        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
//        cloudinary.config.secure = true;
//
//        // Creating a FileChooser to select the file
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle(title);
//        File selectedFile = fileChooser.showOpenDialog(new Stage());
//
//        File file = new File(selectedFile.toString());
//        String fileName = file.getName();
//        fileName = fileName.length() > 15 ? fileName.substring(0, 15) : fileName;
//        fileName = fileName + "...";
//
//        if (selectedFile != null) {
//            try {
//                // Uploading the file to Cloudinary
//                Map<String, Object> uploadParams = new HashMap<>();
//                uploadParams.put("resource_type", "auto");
//
//                Map<?, ?> uploadResult = cloudinary.uploader().upload(selectedFile, uploadParams);
//
//                // Accessing the public URL of the uploaded file
//                String publicUrl = (String) uploadResult.get("secure_url");
//
//                buttonUpdate.setText(fileName);
//
//                // You can now use 'publicUrl' for further processing or display
//
//                System.out.println("File uploaded successfully. Public URL: " + publicUrl);
//                return publicUrl;
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.err.println("Error uploading the file.");
//            }
//        }
//        return null;
//    }
private String uploadFileMP3(String title, Button buttonUpdate) {
    // Configuring Cloudinary
    String CLOUDINARY_URL = "cloudinary://462886354475568:7IyZ1fMXe--ZoDhLd9hr4zlkGOQ@djfpcyyfe";
    Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
    cloudinary.config.secure = true;

    // Creating a FileChooser to select the file
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3"));
    File selectedFile = fileChooser.showOpenDialog(new Stage());

    if (selectedFile != null) {
        try {
            // Uploading the file to Cloudinary
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", "auto");

            Map<?, ?> uploadResult = cloudinary.uploader().upload(selectedFile, uploadParams);

            // Accessing the public URL of the uploaded file
            String publicUrl = (String) uploadResult.get("secure_url");

            // Set the button text to the file name
            String fileName = selectedFile.getName();
            fileName = fileName.length() > 15 ? fileName.substring(0, 15) : fileName;
            fileName = fileName + "...";
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
    private String uploadFileImage(String title, Button buttonUpdate) {
        // Configuring Cloudinary
        String CLOUDINARY_URL = "cloudinary://462886354475568:7IyZ1fMXe--ZoDhLd9hr4zlkGOQ@djfpcyyfe";
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;

        // Creating a FileChooser to select the image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                // Uploading the image file to Cloudinary
                Map<String, Object> uploadParams = new HashMap<>();
                uploadParams.put("resource_type", "auto");

                Map<?, ?> uploadResult = cloudinary.uploader().upload(selectedFile, uploadParams);

                // Accessing the public URL of the uploaded image file
                String publicUrl = (String) uploadResult.get("secure_url");

                // Set the button text to the file name
                String fileName = selectedFile.getName();
                fileName = fileName.length() > 15 ? fileName.substring(0, 15) : fileName;
                fileName = fileName + "...";
                buttonUpdate.setText(fileName);

                // You can now use 'publicUrl' for further processing or display

                System.out.println("Image uploaded successfully. Public URL: " + publicUrl);
                return publicUrl;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error uploading the image file.");
            }
        }
        return null;
    }


    private void create_Song(Integer userId, ActionEvent event) {

        try (Connection connection = ConnectDB.getConnection()) {
            String songName = nameSong.getText();
            String pathSong = urlSong;
            String thumbnail = urlThumbnail;
            String ownerArtist = "Giang"; // You might want to get this dynamically based on user input or authentication
            String kindOfSong = genreComboBox.getValue();

            // Định dạng ngày tháng theo "dd/MM/yyyy"
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);
            Date sqlDate = Date.valueOf(currentDate);

            if (songName.isEmpty() || pathSong.isEmpty() || thumbnail.isEmpty() || kindOfSong.isEmpty()) {
                showAlert("Lỗi", "Thông tin bài nhạc bị khiếm khuyết!", Alert.AlertType.ERROR);
            } else {
                String sql = "INSERT INTO song (nameSong, authorId, dateCreated, pathSong, kindOfSong, totalLike, pathImg, privacy, desAdmin, dateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String privacy_code = "P2";
                String defaultDesAdmin = "Riêng tư";
                if(privacy_comboBox.getValue().equals("Public")) {
                    privacy_code = "P3";
                    defaultDesAdmin = "Đang kiểm duyệt!";
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, songName);
                    preparedStatement.setInt(2,userId);
                    preparedStatement.setString(3, formattedDate);
                    preparedStatement.setString(4, pathSong);
                    preparedStatement.setString(5, kindOfSong);
                    preparedStatement.setInt(6,0);
                    preparedStatement.setString(7, thumbnail);
                    preparedStatement.setString(8, privacy_code);
                    preparedStatement.setString(9, defaultDesAdmin);
                    preparedStatement.setDate(10, sqlDate);


                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        if(privacy_comboBox.getValue().equals("Public")) {
                            showAlert("Thành công", "Bài hát của bạn đã được thêm, vui lòng chờ quản trị viên kiểm duyệt để công khai bài hát đến mọi người!", Alert.AlertType.INFORMATION);
                        }else{
                            showAlert("Thành công", "Bài hát đã được thêm thành công!", Alert.AlertType.INFORMATION);
                        }

                        // You can add further actions or UI updates here after successful insertion
                    } else {
                        showAlert("Lỗi", "Không thể thêm bài hát!", Alert.AlertType.ERROR);
                    }
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

                    // Load a new instance of BottomClient
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/Fxml/Client/MyMusic.fxml"));
                    Parent viewBottomClient;
                    try {
                        viewBottomClient = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    borderPane.setCenter(viewBottomClient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi không xác định!", Alert.AlertType.ERROR);
        }
    }

    private void getDataSongUpload (Integer userId) {
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "SELECT * FROM song " +
                    "JOIN user ON song.authorId = user.userId " +
                    "JOIN code ON song.privacy = code.keyCode " +
                    "WHERE song.authorId = ? AND song.authorId = user.userId";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int songId = resultSet.getInt("songId");
                        String nameSong = resultSet.getString("nameSong");
                        String nameAuthor = resultSet.getString("nameUser");
                        int authorId = resultSet.getInt("authorId");
                        int abumId = resultSet.getInt("playListId");
                        String dateCreated = resultSet.getString("dateCreated");
                        String pathSong = resultSet.getString("pathSong");
                        String pathImg = resultSet.getString("pathImg");
                        String kindOfSong = resultSet.getString("kindOfSong");
                        int playListId = resultSet.getInt("playListId");
                        int totalLike = resultSet.getInt("totalLike");
                        String privacy = resultSet.getString("code.value");
                        String desAdmin = resultSet.getString("desAdmin");
                        ItemSong itemSong = new ItemSong(songId, nameSong, nameAuthor, authorId, abumId, dateCreated, totalLike, pathSong, pathImg, kindOfSong, playListId, privacy, desAdmin);
                        listSong.add(itemSong);


                    }
                    for (int i = 0; i < listSong.size(); i++) {

                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/Fxml/Client/SongView/CheckSongItem.fxml"));

                        try {
                            AnchorPane hBox = fxmlLoader.load();
                            CheckSongItem cic = fxmlLoader.getController();
                            cic.setData(listSong.get(i));
                            song_upload.getChildren().add(hBox);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }catch (Exception e) {
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
