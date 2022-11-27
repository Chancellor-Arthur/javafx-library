package com.republic.library.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneController {
    public void switchToRegistrationScene(ActionEvent event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Registration.fxml");
    }

    public void switchToAuthorizationScene(ActionEvent event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Authorization.fxml");
    }

    public void defaultSwitch(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void init(Stage stage) throws IOException {
        FXMLLoader authorizationLoader = new FXMLLoader(
                getClass().getResource("/com/republic/library/scenes/Authorization.fxml"));
        Scene scene = new Scene(authorizationLoader.load(), 800, 600);
        stage.setTitle("Library");
        stage.setScene(scene);
        stage.show();
    }
}
