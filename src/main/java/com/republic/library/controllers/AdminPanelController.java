package com.republic.library.controllers;

import com.republic.library.db.DBManager;
import com.republic.library.entities.Log;
import com.republic.library.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class AdminPanelController {

    @FXML
    private Button ButtonGoToLibrary;

    @FXML
    private TableColumn<Log, Integer> TableColumnBook;

    @FXML
    private TableColumn<Log, Date> TableColumnDate;

    @FXML
    private TableColumn<Log, Integer> TableColumnInitiator;

    @FXML
    private TableColumn<User, Boolean> TableColumnIsAdmin;

    @FXML
    private TableColumn<User, String> TableColumnLogin;

    @FXML
    private TableColumn<Log, String> TableColumnNewValue;

    @FXML
    private TableColumn<Log, String> TableColumnOldValue;

    @FXML
    private TableColumn<User, String> TableColumnPassword;

    @FXML
    private TableView<Log> TableViewLogs;

    @FXML
    private TableView<User> TableViewUsers;

    @FXML
    void GoToLibrary(ActionEvent event) throws IOException {
        new SceneController().switchToLibraryScene(event);
    }

    @FXML
    public void initialize() throws SQLException {
        showLogs();
        showUsers();
    }

    private void showLogs() throws SQLException {
        ObservableList<Log> list = getLogList();

        TableColumnInitiator.setCellValueFactory(new PropertyValueFactory<Log, Integer>("initiatorId"));
        TableColumnBook.setCellValueFactory(new PropertyValueFactory<Log, Integer>("targetId"));
        TableColumnNewValue.setCellValueFactory(new PropertyValueFactory<Log, String>("newValue"));
        TableColumnOldValue.setCellValueFactory(new PropertyValueFactory<Log, String>("oldValue"));
        TableColumnDate.setCellValueFactory(new PropertyValueFactory<Log, Date>("createdAt"));

        TableViewLogs.setItems(list);
    }

    private void showUsers() throws SQLException {
        ObservableList<User> list = getUserList();

        TableColumnLogin.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        TableColumnPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        TableColumnIsAdmin.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isAdmin"));

        TableViewUsers.setItems(list);
    }

    public ObservableList<Log> getLogList() throws SQLException {
        ObservableList<Log> logList = FXCollections.observableArrayList();

        var logs = DBManager.getAllLogs();
        while (logs.next()) {
            var log = new Log
                    (
                            logs.getInt("id"),
                            logs.getString("action"),
                            logs.getInt("initiator_id"),
                            logs.getInt("target_id"),
                            logs.getString("new_value"),
                            logs.getString("old_value"),
                            logs.getTimestamp("created_at")
                    );
            logList.add(log);
        }
        return logList;
    }

    public ObservableList<User> getUserList() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();

        var users = DBManager.getAllUsers();
        while (users.next()) {
            var user = new User
                    (
                            users.getInt("id"),
                            users.getString("login"),
                            users.getString("password"),
                            users.getBoolean("is_admin")
                    );
            userList.add(user);
        }
        return userList;
    }
}
