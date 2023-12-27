package com.example.app;

import com.example.app.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage stage) {

        Model.getInstance().getViewFactory().showLoginWindow();
    }

}
