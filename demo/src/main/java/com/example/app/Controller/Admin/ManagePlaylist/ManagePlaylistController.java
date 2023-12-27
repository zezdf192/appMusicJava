package com.example.app.Controller.Admin.ManagePlaylist;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Models.Admin.ItemAlbum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagePlaylistController implements Initializable {

    public VBox vbox;
    public Button add_btn;
    public ComboBox arrange_comboBox;
    public TextField text_find;
    public ComboBox find_comboBox;
    public Button find_btn;

    public List<ItemAlbum> listAlbum = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> role = FXCollections.observableArrayList(
                "Tìm kiếm theo tên", "Tìm kiếm theo số lượng bài hát >=", "Tìm kiếm theo số lượng bài hát <="
        );
        ObservableList<String> range = FXCollections.observableArrayList(
                "Không sắp xếp",
                "Sắp xếp tăng dần theo tên",  "Sắp xếp giảm dần theo tên",
                "Sắp xếp tăng dần theo số bài hát", "Sắp xếp giảm dần theo số bài hát"
        );
        find_comboBox.setItems(role);
        find_comboBox.setValue("Tìm kiếm theo tên");

        arrange_comboBox.setItems(range);
        arrange_comboBox.setValue("Không sắp xếp");

        callAPI();
        renderListUser();
       add_btn.setOnAction(event -> handleCreateNewAlbum());
        find_btn.setOnAction(event -> findData());
    }

    public void callAPI() {
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "SELECT * FROM playlist";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("playlistId");
                String thumbnail = resultSet.getString("thumbnailPlaylist");
                String name = resultSet.getString("namePlaylist");
                int authorId = resultSet.getInt("authorId");
                int quantitySong = resultSet.getInt("quantitySong");
                String description = resultSet.getString("description");

                ItemAlbum album = new ItemAlbum(id, thumbnail,  name, authorId, quantitySong, description);
                listAlbum.add(album);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderListUser() {

        for (int i = 0; i < listAlbum.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Admin/ManagePlaylist/ItemPlaylist.fxml"));

            try {
                AnchorPane hBox = fxmlLoader.load();
                PlaylistItemController cic = fxmlLoader.getController();
                cic.setData(listAlbum.get(i));
                vbox.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTypeFind() {
        String typeFind;
        if (find_comboBox.getValue().equals("Tìm kiếm theo tên")) {
            typeFind = "namePlaylist";
        } else if (find_comboBox.getValue().equals("Tìm kiếm theo số lượng bài hát >=")) {
            typeFind = "quantitySong >= ";
        } else {
            typeFind = "quantitySong <= ";
        }
        return typeFind;
    }

    public String getTypeRange() {

        String typeRange;
        if (arrange_comboBox.getValue().equals("Sắp xếp tăng dần theo tên")) {
            typeRange = "ORDER BY namePlaylist ASC";
        }else if (arrange_comboBox.getValue().equals("Sắp xếp giảm dần theo tên")) {
            typeRange = "ORDER BY namePlaylist DESC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp tăng dần theo số bài hát")) {
            typeRange = "ORDER BY quantitySong ASC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp giảm dần theo số bài hát")) {
            typeRange = "ORDER BY quantitySong DESC";
        } else {
            typeRange = "";
        }

        return typeRange;
    }

    private void findData() {
        String typeFind = getTypeFind();
        String typeRange = getTypeRange();


        try {

            String text = text_find.getText();
            String query = "";
            if(typeFind.equals("namePlaylist")) {
                 query = "SELECT * FROM playlist where " + typeFind + " LIKE ? " + typeRange;
            }else {
                query = "SELECT * FROM playlist where " + typeFind + " ? " + typeRange;
            }


            Connection connection = ConnectDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if(typeFind.equals("namePlaylist")) {
                preparedStatement.setString(1, "%" + text + "%");
            }else {
                preparedStatement.setInt(1, Integer.parseInt(text) );
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            listAlbum.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("playlistId");
                String thumbnail = resultSet.getString("thumbnailPlaylist");
                String name = resultSet.getString("namePlaylist");
                int authorId = resultSet.getInt("authorId");
                int quantitySong = resultSet.getInt("quantitySong");
                String description = resultSet.getString("description");

                ItemAlbum album = new ItemAlbum(id, thumbnail,  name, authorId, quantitySong, description);
                listAlbum.add(album);
            }
            vbox.getChildren().clear();
            renderListUser();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleCreateNewAlbum() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManagePlaylist/CreatePlaylist.fxml"));
        Stage createUserStage = new Stage();
        createStage(loader, createUserStage);

        createUserStage.setOnHiding(event -> {
            updateData();
        });
    }

    private void updateData() {
        vbox.getChildren().clear();
        listAlbum.clear();
        callAPI();
        renderListUser();
    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Create new user");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
