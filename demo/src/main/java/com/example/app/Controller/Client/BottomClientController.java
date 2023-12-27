package com.example.app.Controller.Client;

import com.example.app.ConnectDB.ConnectDB;
import com.example.app.Controller.Data;

import com.example.app.Controller.LoginController;
import com.example.app.Controller.SignupController;
import com.example.app.Models.Model;
import com.example.app.Models.Playlist.ListPlayList;
import com.example.app.Models.Playlist.PlaylistItem;
import com.example.app.Models.Song.ListSongPlaying;
import com.example.app.Models.Song.Song;

import com.example.app.Models.User.User;
import com.example.app.Views.ClientMenuOptions;
import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;


public class BottomClientController implements Initializable {

    public Media media;
    public static MediaPlayer mediaPlayer;
    public Label nameSong;
    public ProgressBar progress_song;
    public Label time_end;
    // public Label play_btn;
    public Text time_start;
    public AnchorPane nameSongContainer;
    public ProgressBar volume_song;
    public Label nameUser;
    public ImageView img;
    public Label random_song;
    public Label prev_song;
    public Label next_song;
    public Label repeat_song;

    private boolean playing = true;
    private boolean isRepeat ;
    private boolean isRandom ;
    private Song song;

    public FontAwesomeIconView likeSong;
    public FontAwesomeIconView play_btn;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isRepeat = Data.getDataGLobal.dataGlobal.isRepeat();
        if(isRepeat) {
            repeat_song.setTextFill(Color.GREEN);
        }else {
            repeat_song.setTextFill(Color.BROWN);
        }
        isRandom = Data.getDataGLobal.dataGlobal.isRandom();
        if(isRandom) {
            random_song.setTextFill(Color.GREEN);
        }else {
            random_song.setTextFill(Color.BROWN);
        }
        song = Data.getDataGLobal.dataGlobal.getSong();
        playSong();
        volume_song.setProgress(Data.getDataGLobal.dataGlobal.getVolumeValue());
        addListener();

        Integer songId = song.getSongId();


        User user = Data.getDataGLobal.dataGlobal.getCurrentUser();
        Integer userId = (user != null) ? user.getUserId() : null;

        checkIfSongIsLiked(songId, userId);

        likeSong.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (userId != null) {
                handleLikeSongClicked(event, songId, userId);
            } else {
                // Handle the case where userId is null (optional)
                System.out.println("UserId not found!");
            }
        });
        // Tải ảnh từ đường dẫn

        handleActionSong();
    }

    private void handleActionSong() {
        repeat_song.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isRepeat) {
                    repeat_song.setTextFill(Color.BROWN);
                    isRepeat = false;
                }else {
                    repeat_song.setTextFill(Color.GREEN);
                    isRepeat = true;
                }
            }
        });

        random_song.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isRandom) {
                    random_song.setTextFill(Color.BROWN);
                    isRandom = false;
                }else {
                    random_song.setTextFill(Color.GREEN);
                    isRandom = true;

                }
            }
        });

        next_song.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               nextSong();
            }
        });

        prev_song.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prevSong();
            }
        });

    }

    protected void addListener() {
        play_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(playing) {
                    mediaPlayer.pause();
                    playing = false;
                }else {
                    playing = true;
                    mediaPlayer.play();
                }

            }
        });
        // Xử lý Porgress
        progress_song.setOnMousePressed(event -> {
            //Xử lý khi ấn chuột
            updateProgressBarValue(event);

        });

        progress_song.setOnMouseDragged(event -> {
            // Xử lý khi chuột được kéo (ví dụ: cập nhật giá trị ProgressBar dựa trên vị trí chuột)
            updateProgressBarValue(event);
        });

        //Xử lý volume
        volume_song.setOnMouseDragged(event -> {
            updateVolume(event);
        });

        volume_song.setOnMousePressed(event -> {
            updateVolume(event);
        });
    }

    private void updateVolume(MouseEvent event) {
        double mouseX = event.getX();
        double progressVolumeWidth = volume_song.getWidth();
        double progressVolumeValue = (mouseX / progressVolumeWidth);

        // Ensure the progress value is between 0.0 and 1.0

        progressVolumeValue = Math.min(1.0, Math.max(0.0, progressVolumeValue));
        Data.getDataGLobal.dataGlobal.setVolumeValue(progressVolumeValue);
        volume_song.setProgress(progressVolumeValue);
        mediaPlayer.setVolume(progressVolumeValue);
    }

    private void updateProgressBarValue(MouseEvent event) {
        double mouseX = event.getX();
        double progressBarWidth = progress_song.getWidth();
        double progressBarValue = (mouseX / progressBarWidth);

        // Ensure the progress value is between 0.0 and 1.0
        progressBarValue = Math.min(1.0, Math.max(0.0, progressBarValue));

        // Set the progress to the ProgressBar
        progress_song.setProgress(progressBarValue);

        // Seek the media player to the corresponding time
        Duration totalTime = mediaPlayer.getTotalDuration();
        Duration seekTime = new Duration((long) (progressBarValue * totalTime.toMillis()));
        mediaPlayer.seek(seekTime);
    }

    public void playSong() {
        try {
            if (!song.getPathSong().isEmpty()) {
                stopCurrentSong(); // Stop the current song
                playMp3FromUrl(song.getPathSong());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopCurrentSong() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.dispose();

            } catch (Exception e) {
                // Handle exceptions if necessary
            }
        }
    }

    public void playMp3FromUrl(String file) throws Exception {

        //SongItemHome song = findSongById(listMusic, file);
        URI uri = URI.create(file);
        media = new Media(uri.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(Data.getDataGLobal.dataGlobal.getVolumeValue());
        mediaPlayer.setOnEndOfMedia(() -> {

            mediaPlayer.stop();
            if (isRepeat) {
                repeatSong();
            }else {
                if (isRandom) {
                    randomSong();
                }else {
                    nextSong();
                }
            }

        });

        mediaPlayer.play();
        nameSong.setText(song.getNameSong());
        nameUser.setText(song.getNameAuthor());

        Image image = new Image(song.getPathImg());

        // Đặt ảnh cho đối tượng ImageView
        img.setImage(image);
        updateLabels();
    }

    private void randomSong() {
        time_start.setText("0:00");
        Random random = new Random();
        int min = 0;
        int max = ListSongPlaying.SongListGlobal.songList.getCount() - 1;
        int randomIntInRange = random.nextInt(max - min + 1) + min;

        song = ListSongPlaying.SongListGlobal.songList.getSong(randomIntInRange);
        playSong();
    }

    private void repeatSong() {
        time_start.setText("0:00");
        playSong();
    }

    public void nextSong() {
        time_start.setText("0:00");

        int stt = ListSongPlaying.SongListGlobal.songList.getSttPlaySong();

        if(stt == ListSongPlaying.SongListGlobal.songList.getCount() - 1) {
            ListSongPlaying.SongListGlobal.songList.setSttPlaySong(0);
            song = ListSongPlaying.SongListGlobal.songList.getSong(0);
        }else {
            ListSongPlaying.SongListGlobal.songList.setSttPlaySong(stt + 1);
            song = ListSongPlaying.SongListGlobal.songList.getSong(stt + 1);
        }

        playSong();

    }

    public void prevSong() {
        time_start.setText("0:00");

        int stt = ListSongPlaying.SongListGlobal.songList.getSttPlaySong();

        if(stt == 0) {
            int newStt = ListSongPlaying.SongListGlobal.songList.getCount() - 1;
            ListSongPlaying.SongListGlobal.songList.setSttPlaySong(newStt);
            song = ListSongPlaying.SongListGlobal.songList.getSong(newStt);
        }else {
            ListSongPlaying.SongListGlobal.songList.setSttPlaySong(stt - 1);
            song = ListSongPlaying.SongListGlobal.songList.getSong(stt - 1);
        }

        playSong();
    }

    private void updateLabels() {
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                Duration totalTime = mediaPlayer.getTotalDuration();

                String currentTimeStr = formatDuration(currentTime);
                String totalTimeStr = formatDuration(totalTime);

                double progress = currentTime.toMillis() / totalTime.toMillis() ;
                progress_song.setProgress(progress);
                time_start.setText(currentTimeStr);
                time_end.setText(totalTimeStr);

            });
        }
    }

    private String formatDuration(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }


    // Handle liked Song

    private void handleLikeSongClicked(MouseEvent event, Integer songId, Integer userId) {
        // Xử lý sự kiện khi biểu tượng trái tim được bấm
        Color newColor = Color.web("#7230e4");
        if (likeSong.getFill().equals(Color.WHITE)) {
            // Nếu trạng thái hiện tại là trắng, đổi màu sang xanh
            likeSong.setFill(newColor);

            try (Connection connection = ConnectDB.getConnection()) {
                String sql = "INSERT INTO likesong (songId, userId) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, songId);
                    preparedStatement.setInt(2, userId);

                    int rowsAffected = preparedStatement.executeUpdate();

                    // Kiểm tra xem truy vấn đã thành công hay không
                    if (rowsAffected > 0) {
                        System.out.println("Like playlist successfully added.");
                    } else {
                        System.out.println("Failed to like playlist.");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception as needed (log or show user-friendly message)
            }
        } else {
            // Nếu trạng thái hiện tại không phải là trắng, đổi về trắng
            likeSong.setFill(Color.WHITE);

            try (Connection connection = ConnectDB.getConnection()) {
                String deleteLikeSong = "DELETE from likesong where songId = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteLikeSong)) {
                    preparedStatement.setInt(1, songId);
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Kiểm tra xem truy vấn đã thành công hay không
                    if (rowsAffected > 0) {
                        System.out.println("Like playlist successfully added.");
                    } else {
                        System.out.println("Failed to like playlist.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Update UI Collection Page

            Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.COLLECTION);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();

            // Load a new instance of BottomClient
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Fxml/Client/Collection.fxml"));
            Parent viewBottomClient;
            try {
                viewBottomClient = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            borderPane.setCenter(viewBottomClient);
        }

        // Thêm các hành động khác sau khi nhấn vào biểu tượng trái tim (nếu cần)
    }

    private void checkIfSongIsLiked(Integer songId, Integer userId) {
        Color newColor = Color.web("#7230e4");
        try (Connection connection = ConnectDB.getConnection()) {
            String checkLikeSong = "SELECT * FROM likesong WHERE songId = ? AND userId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(checkLikeSong)) {
                preparedStatement.setInt(1, songId);
                preparedStatement.setInt(2, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Bài hát đã được thích, đặt màu cho hình trái tim
                        likeSong.setFill(newColor);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed (log or show user-friendly message)
        }
    }
}
