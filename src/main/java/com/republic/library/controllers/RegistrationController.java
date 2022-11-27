package com.republic.library.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private Button ButtonGoToSignIn;

    @FXML
    private Button ButtonSignUp;

    @FXML
    private TextField InputFieldLogin;

    @FXML
    private PasswordField InputFieldPassword;

    @FXML
    private PasswordField InputFieldPasswordAgain;

    @FXML
    void GoToSignIn(ActionEvent event) throws IOException {
        new SceneController().switchToAuthorizationScene(event);
    }

}
