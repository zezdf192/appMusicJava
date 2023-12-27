package com.example.app.Controller.Admin.CheckSong;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Admin.ManageSong.ItemSongController;
import com.example.app.Models.Admin.ItemSong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CheckSongController implements Initializable {
    public VBox vbox;
    public ComboBox arrange_comboBox;
    public TextField text_find;
    public ComboBox find_comboBox;
    public Button find_btn;

    public List<ItemSong> listSong = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> role = FXCollections.observableArrayList(
                "Tìm kiếm theo tên", "Tìm kiếm theo thể loại", "Tìm kiếm theo ngày tạo(dd/mm/yyyy)", "Tìm kiếm theo chế độ"
        );
        ObservableList<String> range = FXCollections.observableArrayList(
                "Sắp xếp tăng dần theo ngày tạo","Sắp xếp giảm dần theo ngày tạo",
                "Sắp xếp tăng dần theo tên",  "Sắp xếp giảm dần theo tên",
                "Sắp xếp tăng dần theo lượt like", "Sắp xếp giảm dần theo lượt like",
                "Không sắp xếp"
        );
        find_comboBox.setItems(role);
        find_comboBox.setValue("Tìm kiếm theo tên");

        arrange_comboBox.setItems(range);
        arrange_comboBox.setValue("Không sắp xếp");

        callAPI();
        renderListSong();
        find_btn.setOnAction(event -> findData());
    }

    public void callAPI() {
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "SELECT * \n" +
                    "FROM song\n" +
                    "JOIN code ON song.privacy = code.keyCode\n" +
                    "JOIN user ON user.userId = song.authorId and song.privacy = 'P3';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                int authorId = resultSet.getInt("authorId");
                int abumId = resultSet.getInt("albumId");
                String dateCreated = resultSet.getString("dateCreated");
                String pathSong = resultSet.getString("pathSong");
                String pathImg = resultSet.getString("pathImg");
                String kindOfSong = resultSet.getString("kindOfSong");
                int playListId = resultSet.getInt("playListId");
                int totalLike = resultSet.getInt("totalLike");
                String privacy = resultSet.getString("code.value");
                String desAdmin = resultSet.getString("desAdmin");

                ItemSong itemSong = new ItemSong(id, nameSong, nameAuthor, authorId, abumId, dateCreated, totalLike, pathSong, pathImg, kindOfSong, playListId, privacy, desAdmin);
                listSong.add(itemSong);

            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderListSong() {

        for (int i = 0; i < listSong.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/Fxml/Admin/CheckSong/CheckSongItem.fxml"));

            try {
                AnchorPane hBox = fxmlLoader.load();
                CheckSongItemController cic = fxmlLoader.getController();
                cic.setData(listSong.get(i));
                vbox.getChildren().add(hBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTypeFind() {
        String typeFind;
        if (find_comboBox.getValue().equals("Tìm kiếm theo thể loại")) {
            typeFind = "kindOfSong";
        } else if (find_comboBox.getValue().equals("Tìm kiếm theo ngày tạo(dd/mm/yyyy)")) {
            typeFind = "dateCreated";
        } else if (find_comboBox.getValue().equals("Tìm kiếm theo chế độ")) {
            typeFind = "code.value";
        } else {
            typeFind = "nameSong";
        }
        return typeFind;
    }

    public String getTypeRange() {

        String typeRange;
        if (arrange_comboBox.getValue().equals("Sắp xếp tăng dần theo tên")) {
            typeRange = "ORDER BY nameSong ASC";
        }else if (arrange_comboBox.getValue().equals("Sắp xếp giảm dần theo tên")) {
            typeRange = "ORDER BY nameSong DESC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp tăng dần theo lượt like")) {
            typeRange = "ORDER BY totalLike ASC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp giảm dần theo lượt like")) {
            typeRange = "ORDER BY totalLike DESC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp tăng dần theo ngày tạo")) {
            typeRange = "ORDER BY dateTime ASC";
        } else if (arrange_comboBox.getValue().equals("Sắp xếp giảm dần theo ngày tạo")) {
            typeRange = "ORDER BY dateTime DESC";
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
            String query = "SELECT * FROM song JOIN code ON song.privacy = code.keyCode JOIN user ON user.userId = song.authorId and song.privacy = 'P3' AND " + typeFind + " LIKE ? " + typeRange;

            Connection connection = ConnectDB.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + text + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            listSong.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                int authorId = resultSet.getInt("authorId");
                int abumId = resultSet.getInt("abumId");
                String dateCreated = resultSet.getString("dateCreated");
                String pathSong = resultSet.getString("pathSong");
                String pathImg = resultSet.getString("pathImg");
                String kindOfSong = resultSet.getString("kindOfSong");
                int playListId = resultSet.getInt("playListId");
                int totalLike = resultSet.getInt("totalLike");
                String privacy = resultSet.getString("code.value");
                String desAdmin = resultSet.getString("desAdmin");

                ItemSong itemSong = new ItemSong(id, nameSong, nameAuthor, authorId, abumId, dateCreated, totalLike, pathSong, pathImg, kindOfSong, playListId, privacy, desAdmin);
                listSong.add(itemSong);
            }
            vbox.getChildren().clear();
            renderListSong();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
