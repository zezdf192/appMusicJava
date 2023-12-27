package com.example.app.Controller.Admin.ManageUser;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class UpdateUserController implements Initializable {
    private int id;
    public TextField name_input;
    public TextField email_input;
    public TextField password_input;
    public ComboBox role_comboBox;
    public Button update_btn;


    public UpdateUserController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> role = FXCollections.observableArrayList(
                "Người dùng", "Nghệ sĩ", "Quản trị viên"
        );
        role_comboBox.setItems(role);
        role_comboBox.setValue("Người dùng");

        update_btn.setOnAction(event -> updateUser(event));

        updateInfoUser();
    }

    private void updateInfoUser() {
        ItemUser user = Data.getDataGLobal.dataGlobal.getCurrentEditUser();
        name_input.setText(user.getName());
        email_input.setText(user.getEmail());
        password_input.setText("123123");
        role_comboBox.setValue(user.getRole());
        id = user.getId();
    }

    private void updateUser(ActionEvent event) {
        String roleId;
        if(role_comboBox.getValue().equals("Người dùng")) {
            roleId = "R2";
        } else if (role_comboBox.getValue().equals("Nghệ sĩ")) {
            roleId = "R3";
        }else {
            roleId = "R1";
        }
        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "UPDATE user SET role = ?, nameUser = ? WHERE userId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roleId);
                preparedStatement.setString(2, name_input.getText());
                preparedStatement.setInt(3, id);
                int rowsAffected = preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemUser user = new ItemUser(id, name_input.getText(), email_input.getText(), roleId, "male");
        Data.getDataGLobal.dataGlobal.setCurrentEditUser(user);
        Stage stage = (Stage) update_btn.getScene().getWindow();
        stage.close();
    }
}
