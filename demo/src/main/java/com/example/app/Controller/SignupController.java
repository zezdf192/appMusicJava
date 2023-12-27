package com.example.app.Controller;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.app.Models.User.User;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignupController implements Initializable {
    public TextField email_field;
    public PasswordField password_field;
    public TextField userName_field;
    public PasswordField confirmPassword_field;
    public Button submit_signup;
    public Button change_login;

//    private static User user; // Đặt làm biến toàn cục

//    public static User getUser() {
//        return user;
//    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        submit_signup.setOnAction(event -> onSignup());
        change_login.setOnAction(actionEvent -> change_login());
    }

    private void onSignup () {

        try (Connection connection = ConnectDB.getConnection()) {
            String email = email_field.getText();
            System.out.println(email);
            String userName = userName_field.getText();
            System.out.println((userName));
            String password = password_field.getText();
            String confirmPassword = confirmPassword_field.getText();

            LocalDateTime currentDateTime = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String regisDate = currentDateTime.format(formatter);

            String gender = "Male";


            if (email.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                showAlert("Lỗi", "Không thể đăng ký! \nĐang có trường dữ liệu bị khuyết thiếu thông tin", Alert.AlertType.ERROR);
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Lỗi", "Mật khẩu xác nhận không khớp!", Alert.AlertType.ERROR);
                return;
            }

            if (emailExists(email, connection)) {
                showAlert("Lỗi", "Email đã được sử dụng!", Alert.AlertType.ERROR);
                return;
            }

            String sql = "INSERT INTO user (role, nameUser, password, email, gender, regisDate) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "R2");
                statement.setString(2, userName);
                statement.setString(3, hashPassword(password));
                statement.setString(4, email);
                statement.setString(5, gender);
                statement.setString(6, regisDate);

                // Thực hiện truy vấn
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    userName_field.setText("Tạo tài khoản thành công");

                    try (connection) {
                        String getDataUser = "SELECT * FROM user where email = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, email);

                            try (ResultSet resultSet = preparedStatement.executeQuery(getDataUser)) {
                                while (resultSet.next()) {
                                    String nameUser = resultSet.getString("nameUser");
                                    Integer userId = resultSet.getInt("userId");
                                    String userEmail = resultSet.getString("email");
                                    String userGender = resultSet.getString("gender");
                                    String userPasword = resultSet.getString("password");
                                    String role = resultSet.getString("role");

//                                    user = new User();
//
//                                    user.setUserName(nameUser);
//                                    user.setUserEmail(userEmail);
//                                    user.setUserGender(userGender);
//                                    user.setUserId(userId);

                                    User user = new User(userName, userId, userEmail,userGender,userPasword, role);
                                    Data.getDataGLobal.dataGlobal.setCurrentUser(user);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace(); // Handle the exception appropriately
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stage stage = (Stage) submit_signup.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);

                    Model.getInstance().getViewFactory().showClientWindow();
                } else {
                    showAlert("Lỗi", "Không thể tạo tài khoản", Alert.AlertType.ERROR);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi không xác định!", Alert.AlertType.ERROR);
        }
    }

    private  void change_login () {
        Stage stage = (Stage) change_login.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    private boolean emailExists(String email, Connection connection) {
        try {
            String sql = "SELECT * FROM user WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String hashPassword(String password) {
        // Hash the password using BCrypt
        String bcryptResult = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return bcryptResult.toString();
    }

}
