package com.republic.library.controllers;

import com.republic.library.db.DBManager;
import com.republic.library.utils.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

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

    @FXML
    void SignUp(ActionEvent event) throws SQLException, IOException {
        var login = InputFieldLogin.getText().trim();
        var password = InputFieldPassword.getText().trim();
        var passwordAgain = InputFieldPasswordAgain.getText().trim();

        if (login.equals("") || password.equals("") || passwordAgain.equals("")) {
            Alerts.showErrorAlert("Введите логин/пароль/подтверждение пароля!");
            return;
        }

        var oldUser = DBManager.getUser(login);

        if (oldUser.next()) {
            Alerts.showErrorAlert("Логин занят!");
            return;
        }

        if (!password.equals(passwordAgain)) {
            Alerts.showErrorAlert("Пароли не совпадают!");
            return;
        }

        DBManager.createUser(InputFieldLogin.getText().trim(), InputFieldPassword.getText().trim());
        new SceneController().switchToAuthorizationScene(event);
        Alerts.showSuccessAlert("Пользователь создан!");
    }

}
