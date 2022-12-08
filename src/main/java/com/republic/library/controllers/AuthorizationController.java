package com.republic.library.controllers;

import com.republic.library.db.DBManager;
import com.republic.library.utils.Alerts;
import com.republic.library.store.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

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

    @FXML
    void SignIn(ActionEvent event) throws IOException, SQLException {
        var login = InputFieldLogin.getText().trim();
        var password = InputFieldPassword.getText().trim();

        if (login.equals("") || password.equals("")) {
            Alerts.showErrorAlert("Введите логин/пароль!");
            return;
        }

        var user = DBManager.getUser(InputFieldLogin.getText().trim(), InputFieldPassword.getText().trim());
        if (user.next()) {
            Store.isAdmin = user.getBoolean("is_admin");
            Store.userId = user.getInt("id");
            new SceneController().switchToLibraryScene(event);
        } else {
            Alerts.showErrorAlert("Неверный логин/пароль!");
        }
    }

}

