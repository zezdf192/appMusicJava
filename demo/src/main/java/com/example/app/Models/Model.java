package com.example.app.Models;

import com.example.app.Views.ViewAdminFactory;
import com.example.app.Views.ViewFactory;
import javafx.fxml.FXMLLoader;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final ViewAdminFactory viewAdminFactory;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.viewAdminFactory = new ViewAdminFactory();
    }

    public static synchronized Model getInstance() {
        if(model == null) {
            model = new Model();
        }
        return model;
    }


    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ViewAdminFactory getViewAdminFactory() {
        return viewAdminFactory;
    }
}
