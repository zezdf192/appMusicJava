package com.example.app.Controller;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Models.Playlist.ListPlayList;

import com.example.app.Models.Model;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.User.User;
import com.example.app.Models.Song.Song;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginController implements Initializable {


    public Button change_signup;
    public Button login_btn;
    public PasswordField password_field;
    public TextField email_field;
    public FontAwesomeIconView display_pass;
    public Label display_text;

//    private static User user; // Đặt làm biến toàn cục

//    public static User getUser() {
//        return user;
//    }

    private boolean isPasswordVisible = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(actionEvent -> {
            try {
                onLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        change_signup.setOnAction(actionEvent -> change_signup());
        display_pass.setOnMouseClicked(mouseEvent -> display_pass());

        //addSongByDatabase();
        addListSongByDatabase();

    }


    //    private void onLogin() throws SQLException {
//
//        String email = email_field.getText();
//        String password = password_field.getText();
//
//        if (email.isEmpty() || password.isEmpty()) {
//            showAlert("Lỗi", "Vui lòng nhập email và mật khẩu!", Alert.AlertType.ERROR);
//            return;
//        }
//        Connection connection = ConnectDB.getConnection();
//
//        try (connection) {
//            String sql = "SELECT * FROM user where email = ? and password = ?";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, email);
//                preparedStatement.setString(2, password);
//                if (validateLogin(email, password, connection)) {
//                    // Successful login
//                    showAlert("Thông báo", "Đăng nhập thành công!", Alert.AlertType.INFORMATION);
//                    Stage stage = (Stage) login_btn.getScene().getWindow();
//                    Model.getInstance().getViewFactory().closeStage(stage);
////                    Model.getInstance().getViewFactory().showClientWindow();
////                Model.getInstance().getViewAdminFactory().showAdminWindow();
//                } else {
//                    showAlert("Lỗi", "Email hoặc mật khẩu không đúng!", Alert.AlertType.ERROR);
//                }
//                try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                    while (resultSet.next()) {
//                        String userName = resultSet.getString("nameUser");
//                        Integer userId = resultSet.getInt("userId");
//                        String userEmail = resultSet.getString("email");
//                        String userGender = resultSet.getString("gender");
//                        String passwordUser = resultSet.getString("password");
//
//                        String roleCode = resultSet.getString("role");
//
////                        if (!userEmail.equals(email)) {
////                            showAlert("Lỗi", "Email không tồn tại!", Alert.AlertType.ERROR);
////                        }
////                        if (!passwordUser.equals(password)) {
////                            showAlert("Lỗi", "Mật khẩu không đúng!", Alert.AlertType.ERROR);
////
////                        }
//                        if (roleCode.equals("R1")) {
//                            Model.getInstance().getViewAdminFactory().showAdminWindow();
//                        } else {
//                            Model.getInstance().getViewFactory().showClientWindow();
//                        }
//                        User user = new User(userName, userId, userEmail,userGender);
//                        Data.getDataGLobal.dataGlobal.setCurrentUser(user);
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace(); // Handle the exception appropriately
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void onLogin() {
        try (Connection connection = ConnectDB.getConnection()) {
            String email = email_field.getText();
            String password = password_field.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập email và mật khẩu!", Alert.AlertType.ERROR);
                return;
            }

            User user = getUserByEmail(email, connection);

            if (user != null) {
                // Verify the entered password against the stored hashed password
                if (verifyPassword(password, user.getUserPassword())) {
                    // Password is correct
                    showAlert("Thông báo", "Đăng nhập thành công!", Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) login_btn.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);

                    // Determine user role and show the appropriate window
                    if (user.getRole().equals("R1")) {
                        Model.getInstance().getViewAdminFactory().showAdminWindow();
                    } else {
                        Model.getInstance().getViewFactory().showClientWindow();
                    }

                    Data.getDataGLobal.dataGlobal.setCurrentUser(user);
                } else {
                    showAlert("Lỗi", "Email hoặc mật khẩu không đúng!", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Lỗi", "Không tìm thấy người dùng với địa chỉ email này", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi không xác định!", Alert.AlertType.ERROR);
        }
    }

    private  void change_signup () {
        Stage stage = (Stage) change_signup.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showSignupWindow();
    }

    private boolean validateLogin(String email, String password, Connection connection) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If there is at least one row, login is successful
        }
    }
    private User getUserByEmail(String email, Connection connection) {
        try {
            String sql = "SELECT * FROM user WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User(null, null, null ,null, null, null);
                        user.setUserId(resultSet.getInt("userId"));
                        user.setUserName(resultSet.getString("nameUser"));
                        user.setUserEmail(resultSet.getString("email"));
                        user.setUserGender(resultSet.getString("gender"));
                        user.setUserPassword(resultSet.getString("password")); // Get the hashed password from the database
                        user.setRole(resultSet.getString("role"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void addSongByDatabase() {
        ListSongPlaying.SongListGlobal.songList.removeListSong();

        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from song join user on song.authorId = user.userId";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int songId = resultSet.getInt("songId");
                String nameSong = resultSet.getString("nameSong");
                String nameAuthor = resultSet.getString("nameUser");
                String dateCreated =  resultSet.getString("dateCreated");
                String totalLike =  resultSet.getString("totalLike");
                String pathSong =  resultSet.getString("pathSong");
                String pathImg =  resultSet.getString("pathImg");
                String kindOfSong =  resultSet.getString("kindOfSong");
                Song song = new Song(songId, nameSong, nameAuthor, dateCreated, totalLike, pathSong, pathImg, kindOfSong);
                ListSongPlaying.SongListGlobal.songList.addSong(song);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListSongByDatabase() {
        ListPlayList.ListPlayListGlobal.songList.removeSongList();
        try{
            Connection connection = ConnectDB.getConnection();
            String query = "select * from playlist where authorId = 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int playlistId = resultSet.getInt("playlistId");
                String thumbnailPlaylist = resultSet.getString("thumbnailPlaylist");
                String category =  resultSet.getString("category");
                String namePlaylist =  resultSet.getString("namePlaylist");
                int authorId =  resultSet.getInt("authorId");
                int quantitySong =  resultSet.getInt("quantitySong");
                String description =  resultSet.getString("description");

                PlaylistItem playlistItem = new PlaylistItem(playlistId, thumbnailPlaylist, category,
                        namePlaylist, authorId, quantitySong, description);
                ListPlayList.ListPlayListGlobal.songList.addListPlayList(playlistItem);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verifyPassword(String enteredPassword, String hashedPassword) {
        // Use BCrypt to verify the entered password against the stored hashed password
        BCrypt.Result result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), hashedPassword);
        return result.verified;
    }


    public void display_pass() {
        if (isPasswordVisible) {
            display_pass.setGlyphName("EYE_SLASH");
            display_text.setText("");
        } else {
            display_text.setText("Mật khẩu của bạn là: " + password_field.getText());
            display_pass.setGlyphName("EYE");
        }
        isPasswordVisible = !isPasswordVisible;
    }

}
