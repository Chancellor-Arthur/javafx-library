package com.republic.library.controllers;

import com.republic.library.Library;
import com.republic.library.entities.Book;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneController {
    public void switchToRegistrationScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Registration.fxml");
    }

    public void switchToAuthorizationScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Authorization.fxml");
    }

    public void switchToLibraryScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Library.fxml");
    }

    public void switchToBookScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/Book.fxml");
    }

    public void switchToBookCreationScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/BookCreation.fxml");
    }

    public void switchToAdminPanelScene(Event event) throws IOException {
        defaultSwitch(event, "/com/republic/library/scenes/AdminPanel.fxml");
    }

    public void defaultSwitch(Event event, String path) throws IOException {
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
        stage.setTitle("Библиотека");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/republic/library/icon.png"))));
        stage.setScene(scene);
        stage.show();
    }
}
