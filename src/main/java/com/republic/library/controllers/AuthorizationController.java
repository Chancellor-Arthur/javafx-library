package com.republic.library.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthorizationController {

    @FXML
    private Button ButtonSignIn;

    @FXML
    private Button ButtonGoToSignUp;

    @FXML
    private TextField InputFieldLogin;

    @FXML
    private PasswordField InputFieldPassword;

    @FXML
    void GoToSignUp(ActionEvent event) throws IOException {
        new SceneController().switchToRegistrationScene(event);
    }

}

