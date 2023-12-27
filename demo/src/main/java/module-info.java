module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql;
    requires javafx.media;
    requires de.jensd.fx.glyphs.fontawesome;
    requires cloudinary.core;
    requires cloudinary.http44;
    requires com.microsoft.sqlserver.jdbc;
    requires bcrypt;

    opens com.example.app to javafx.fxml;
    exports com.example.app;


    exports com.example.app.Controller;
    exports com.example.app.Controller.Song;
    exports com.example.app.Controller.Admin;
    exports com.example.app.Controller.Client;
    exports com.example.app.Controller.Client.Home;
    exports com.example.app.Models;
    exports com.example.app.Views;
    exports com.example.app.Controller.Client.PlaylistUserCreate;
    exports com.example.app.Controller.Client.Song;
    exports com.example.app.Controller.Client.Playlist;
    exports com.example.app.Controller.Client.Album;

    exports com.example.app.Models.Song;
    exports com.example.app.Models.Playlist;
    exports com.example.app.Controller.Admin.ManageUser;
    exports com.example.app.Controller.Admin.ManageSong;
    exports com.example.app.Controller.Admin.ManageAlbum;
    exports com.example.app.Controller.Admin.ManagePlaylist;
    exports com.example.app.Models.Admin;
    exports com.example.app.Controller.Admin.CheckSong;

}