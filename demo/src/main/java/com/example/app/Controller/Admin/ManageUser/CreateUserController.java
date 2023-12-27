package com.example.app.Controller.Admin.ManageUser;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Models.Admin.ItemUser;
import com.example.app.Models.Model;
import com.example.app.Models.User.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateUserController implements Initializable {

    public TextField name_input;
    public TextField email_input;
    public TextField password_input;
    public TextField repeat_pass_input;
    public ComboBox role_comboBox;
    public Button create_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> role = FXCollections.observableArrayList(
                "Người dùng", "Nghệ sĩ", "Quản trị viên"
        );
        role_comboBox.setItems(role);
        role_comboBox.setValue("Người dùng");

        create_btn.setOnAction(event -> createNewUser(event));
    }

    private void createNewUser(ActionEvent event) {
        if(!password_input.getText().equals(repeat_pass_input.getText())) {
            showAlert("Lỗi", "Mật khẩu nhập lại chưa chính xác!", Alert.AlertType.ERROR);
            return;
        }
        String roleId;
        if(role_comboBox.getValue().equals("Người dùng")) {
            roleId = "R2";
        } else if (role_comboBox.getValue().equals("Nghệ sĩ")) {
            roleId = "R3";
        }else {
            roleId = "R1";
        }

        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "INSERT INTO user (role, nameUser, password, email, gender) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roleId);
                preparedStatement.setString(2,name_input.getText());
                preparedStatement.setString(3, password_input.getText());
                preparedStatement.setString(4, email_input.getText());
                preparedStatement.setString(5, "Male");
                int rowsAffected = preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi không xác định!", Alert.AlertType.ERROR);
        }
        Stage stage = (Stage) create_btn.getScene().getWindow();
        stage.close();



        //showAlert("Hoàn thành", "Tạo mới người dùng thành công!", Alert.AlertType.ERROR);
    }



    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

