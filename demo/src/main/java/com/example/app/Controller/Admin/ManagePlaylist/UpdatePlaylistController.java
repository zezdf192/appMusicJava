package com.example.app.Controller.Admin.ManagePlaylist;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemAlbum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdatePlaylistController implements Initializable {
    public TextField name_input;
    public TextArea des_input;
    public Button upload_thumnail;
    public ImageView img;
    public Button update_btn;
    public ComboBox category_comboBox;
    private int id;
    private String urlThumbnail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateInfoAlbum();

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

        update_btn.setOnAction(event -> updateAlbum(event));
    }

    private void updateInfoAlbum() {
        ItemAlbum album = Data.getDataGLobal.dataGlobal.getCurrentEditAlbum();
        name_input.setText(album.getName());
        des_input.setText(album.getDescription());
        id = album.getId();
        urlThumbnail = album.getThumbnail();

        Image image = new Image(album.getThumbnail());
        img.setImage(image);
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

    private void updateAlbum(ActionEvent event) {

        try (Connection connection = ConnectDB.getConnection()) {
            String nameAlbum = name_input.getText();
            String des = des_input.getText();
            String thumbnail = urlThumbnail;
            String category = String.valueOf(category_comboBox.getValue());


            String sql = "UPDATE playlist SET thumbnailPlaylist = ?, category = ?, namePlaylist = ?, description = ? WHERE playlistId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, thumbnail);
                preparedStatement.setString(2, category);
                preparedStatement.setString(3, nameAlbum);
                preparedStatement.setString(4, des);
                preparedStatement.setInt(5, id);

                int rowsAffected = preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemAlbum oldAlbum = Data.getDataGLobal.dataGlobal.getCurrentEditAlbum();

        ItemAlbum album = new ItemAlbum(oldAlbum.getId(), urlThumbnail, name_input.getText(), oldAlbum.getAuthorId(), oldAlbum.getQuantitySong(), des_input.getText());

        Data.getDataGLobal.dataGlobal.setCurrentEditAlbum(album);

        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
    }
}
