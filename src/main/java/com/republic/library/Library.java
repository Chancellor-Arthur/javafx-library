package com.republic.library;

import com.republic.library.controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Library extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new SceneController().init(stage);
    }

    public static void main(String[] args) {
        launch();
    }}
