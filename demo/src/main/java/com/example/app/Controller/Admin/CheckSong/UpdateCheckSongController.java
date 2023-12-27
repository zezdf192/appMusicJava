package com.example.app.Controller.Admin.CheckSong;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemSong;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class UpdateCheckSongController implements Initializable {
    public TextArea text_field;
    private int id;
    public Button submit_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ItemSong song = Data.getDataGLobal.dataGlobal.getCurrentEditSong();
        id = song.getSongId();
        submit_btn.setOnAction(event -> handle());
    }

    private void handle() {
        try (Connection connection = ConnectDB.getConnection()) {
            String desAdmin = text_field.getText();


            String sql = "UPDATE song SET desAdmin = ?, privacy = 'P2' WHERE songId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, desAdmin);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
            }


            Stage stage = (Stage) submit_btn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
