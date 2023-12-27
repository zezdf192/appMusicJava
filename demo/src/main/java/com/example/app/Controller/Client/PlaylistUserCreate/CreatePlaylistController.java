package com.example.app.Controller.Client.PlaylistUserCreate;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Client.Playlist.PlayListController;
import com.example.app.Controller.Data;
import com.example.app.Models.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CreatePlaylistController extends PlayListController implements Initializable {
    public TextField name_input;
    public Button create_btn;
    public TextArea des_input;
    public Button upload_thumnail;
    public ImageView img;

    private String urlThumbnail;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = user.getUserId();

        upload_thumnail.setOnAction(actionEvent -> {
            urlThumbnail = uploadThumbnail(upload_thumnail);
            Image image = new Image(urlThumbnail);
            img.setImage(image);
        });

        create_btn.setOnAction(event -> {
            try {
                createPlaylist(userId, new ActionEvent());
//                reloadPage(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void createPlaylist(Integer userId, ActionEvent event) throws SQLException {

        Connection connection = ConnectDB.getConnection();
        String sql = "INSERT INTO playlist (thumbnailPlaylist, category, namePlaylist, authorId, quantitySong, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, urlThumbnail);
            preparedStatement.setString(2, "");
            preparedStatement.setString(3, name_input.getText());
            preparedStatement.setInt(4, userId);
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, des_input.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Playlist created successfully.");
                showAlert("Thành công", "Đã thêm playlist vào danh sách!", Alert.AlertType.INFORMATION);
                name_input.setText("");

            } else {
                System.out.println("Failed to create playlist.");
            }
        }

        Stage stage = (Stage) create_btn.getScene().getWindow();
        stage.close();

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

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
