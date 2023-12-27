package com.example.app.Controller.Admin.ManageSong;

import com.cloudinary.Cloudinary;
import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import com.example.app.Models.Admin.ItemUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateSongController implements Initializable {
    private int id;
    public TextField nameSong;
    public Button upload_thumnail;
    public Button upload_song;
    public ComboBox category;
    public Button upload;
    public ComboBox abum;
    public ComboBox playlist;
    public Label artist_txt;
    public ImageView img;
    ObservableList<AbumDB> abums = FXCollections.observableArrayList();
    ObservableList<PlaylistDB> playlists = FXCollections.observableArrayList();

    private String urlThumbnail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBoxAbum();
        setComboBoxPlaylist();
        updateInfoSong();
        ObservableList<String> genres = FXCollections.observableArrayList(
                "Pop", "Rock", "Hip Hop", "R&B", "Country", "Jazz", "Classical"
        );

        category.setItems(genres);

        upload_thumnail.setOnAction(actionEvent -> {
            urlThumbnail = uploadThumbnail(upload_thumnail);
            Image image = new Image(urlThumbnail);
            img.setImage(image);
        });

        upload.setOnAction(event -> updateSong(event));

    }

    private void setComboBoxPlaylist() {
        PlaylistDB non = new PlaylistDB("Không playlist", -1);
        playlists.add(non);
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
        abums.add(non);
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

    private void updateInfoSong() {
        ItemSong song = Data.getDataGLobal.dataGlobal.getCurrentEditSong();

        nameSong.setText(song.getNameSong());
        artist_txt.setText(song.getNameAuthor());
        category.setValue(song.getKindOfSong());
        id = song.getSongId();

        for (int i = 0; i < abums.size(); i++) {
            if(abums.get(i).getId() == song.getAbumId()){
                AbumDB a = new AbumDB(abums.get(i).getName(), song.getAbumId());
                abum.setValue(a);
            }
        }

        for (int i = 0; i < playlists.size(); i++) {
            if(playlists.get(i).getId() == song.getPlaylistId()){
                PlaylistDB a = new PlaylistDB(playlists.get(i).getName(), song.getPlaylistId());
                playlist.setValue(a);
            }
        }

        Image image = new Image(song.getPathImg());
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

    private void updateSong(ActionEvent event) {
        AbumDB ab = (AbumDB) abum.getValue();
        PlaylistDB pl = (PlaylistDB) playlist.getValue();

        try (Connection connection = ConnectDB.getConnection()) {
            String songName = nameSong.getText();
            String thumbnail = urlThumbnail;
            String kindOfSong = String.valueOf(category.getValue());


            String sql = "UPDATE song SET nameSong = ?, albumId = ?, pathImg = ?, kindOfSong = ?, playListId = ? WHERE songId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, songName);
                preparedStatement.setInt(2, ab.getId());
                preparedStatement.setString(3, thumbnail);
                preparedStatement.setString(4, kindOfSong);
                preparedStatement.setInt(5, pl.getId());
                preparedStatement.setInt(6, id);
                int rowsAffected = preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemSong oldSong = Data.getDataGLobal.dataGlobal.getCurrentEditSong();
        ItemSong song = new ItemSong(id, nameSong.getText(), artist_txt.getText(), oldSong.getAuthorId(), ab.getId(),
                oldSong.getDateCreated(), oldSong.getTotalLike(), oldSong.getPathSong()
                , urlThumbnail, oldSong.getKindOfSong(), pl.getId(), oldSong.getPrivacy(), oldSong.getDesAdmin() );

        Data.getDataGLobal.dataGlobal.setCurrentEditSong(song);

        Stage stage = (Stage) upload.getScene().getWindow();
        stage.close();
    }
}
