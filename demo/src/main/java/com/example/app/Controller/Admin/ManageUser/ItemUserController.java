package com.example.app.Controller.Admin.ManageUser;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;
import com.example.app.Models.Admin.ItemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class ItemUserController implements Initializable {
    public Label id;
    public Label name;
    public Label email;
    public Label role;
    public Label gender;
    public Button update_btn;
    public Button remove_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        update_btn.setOnAction(event -> updateUser());
        remove_btn.setOnAction(event -> removeUser(event));
    }

    public void setData(ItemUser user) {
        id.setText(String.valueOf(user.getId()));
        name.setText(user.getName());
        email.setText(user.getEmail());
        role.setText(user.getRole());
        gender.setText(user.getGender());
    }

    public void updateUser() {
        ItemUser user = new ItemUser(Integer.valueOf(id.getText()), name.getText(), email.getText(), role.getText(), gender.getText());
        Data.getDataGLobal.dataGlobal.setCurrentEditUser(user);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ManageUser/UpdateUser.fxml"));
            Stage createUserStage = new Stage();
            createStage(loader, createUserStage);
            createUserStage.setOnHiding(event -> {
                updateData();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateData() {
        ItemUser user = Data.getDataGLobal.dataGlobal.getCurrentEditUser();
        name.setText(user.getName());
        if(user.getRole().equals("R2")) {
            role.setText("Người dùng");
        } else if (user.getRole().equals("R3")) {
            role.setText("Nghệ sĩ");
        }else{
            role.setText("Quản trị viên");
        }

    }

    public void createStage(FXMLLoader loader, Stage stage) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Update new user");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeUser(ActionEvent event) {
        try (Connection connection = ConnectDB.getConnection()) {
            String sql = "DELETE FROM user WHERE userId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.valueOf(id.getText()));
                int rowsAffected = preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Fxml/Admin/ManageUser/ManageUser.fxml"));
        Parent viewBottomClient;
        try {
            viewBottomClient = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        borderPane.setCenter(viewBottomClient);

    }
}
