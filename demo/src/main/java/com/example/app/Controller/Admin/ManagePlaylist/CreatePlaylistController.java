package com.example.app.Controller.Admin.ManagePlaylist;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CreatePlaylistController implements Initializable {
    public TextField name_input;
    public Button create_btn;
    public Button upload_thumnail;
    public TextArea des_input;
    public ImageView img;
    public ComboBox category_comboBox;

    private String urlThumbnail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> genres = FXCollections.observableArrayList(
                "Pop", "Rock", "Hip Hop", "R&B", "Country", "Jazz", "Classical"
        );


        category_comboBox.setItems(genres);
        category_comboBox.setValue(genres.get(0));

        upload_thumnail.setOnAction(actionEvent -> {
            urlThumbnail = uploadThumbnail(upload_thumnail);
            Image image = new Image(urlThumbnail);
            img.setImage(image);
        });

        create_btn.setOnAction(actionEvent -> create_album());
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

    private String uploadThumbnail(Button buttonUpdate) {
        return uploadFile("Select a Thumbnail File", buttonUpdate);
    }

    private void create_album() {

        try (Connection connection = ConnectDB.getConnection()) {
            String nameAlbum = name_input.getText();
            String des = des_input.getText();
            String thumbnail = urlThumbnail;
            String category = String.valueOf(category_comboBox.getValue());


            if (nameAlbum.isEmpty() ||  thumbnail.isEmpty() || des.isEmpty()) {
                showAlert("Lỗi", "Thông tin bài nhạc bị khiếm khuyết!", Alert.AlertType.ERROR);
            } else {

                String sql = "INSERT INTO playlist (thumbnailPlaylist, category, namePlaylist, authorId, quantitySong, description) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, urlThumbnail);
                    preparedStatement.setString(2, category);
                    preparedStatement.setString(3, nameAlbum);
                    preparedStatement.setInt(4, 1);
                    preparedStatement.setInt(5, 0);
                    preparedStatement.setString(6, des);

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
        Stage stage = (Stage) create_btn.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
