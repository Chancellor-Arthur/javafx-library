package com.republic.library.controllers;

import com.republic.library.db.DBManager;
import com.republic.library.entities.Book;
import com.republic.library.store.Store;
import com.republic.library.utils.Alerts;
import com.republic.library.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class BookCreationController {

    @FXML
    private Button ButtonGoToLibrary;

    @FXML
    private Button ButtonCreateBook;

    @FXML
    private TextArea TextAreaDescription;

    @FXML
    private TextField TextFieldAuthor;

    @FXML
    private TextField TextFieldCategories;

    @FXML
    private TextField TextFieldLink;

    @FXML
    private TextField TextFieldPages;

    @FXML
    private TextField TextFieldTitle;

    @FXML
    private TextField TextFieldYear;

    @FXML
    void GoToLibrary(ActionEvent event) throws IOException {
        new SceneController().switchToLibraryScene(event);
    }

    @FXML
    void CreateBook(ActionEvent event) throws SQLException {
        var categories = Utils.checkAndReturnCategories(TextFieldCategories.getText());

        if (!Utils.isValidURL(TextFieldLink.getText().trim())) {
            Alerts.showErrorAlert("Недействительный URL!");
            return;
        }

        if (Integer.parseInt(TextFieldYear.getText().trim()) < 0) {
            Alerts.showErrorAlert("Невозможный год!");
            return;
        }

        if (Integer.parseInt(TextFieldPages.getText().trim()) <= 0) {
            Alerts.showErrorAlert("Невозможное количество страниц!");
            return;
        }

        var book = new Book
                (
                        -1,
                        Store.userId,
                        TextFieldTitle.getText().trim(),
                        TextFieldAuthor.getText().trim(),
                        Integer.parseInt(TextFieldYear.getText().trim()),
                        Integer.parseInt(TextFieldPages.getText().trim()),
                        TextAreaDescription.getText().trim(),
                        TextFieldLink.getText().trim(),
                        categories
                );
        DBManager.createBook(book);
        clearFields();
        Alerts.showSuccessAlert("Книга успешно создана!");
    }

    void clearFields() {
        TextFieldTitle.setText(null);
        TextFieldAuthor.setText(null);
        TextFieldYear.setText(null);
        TextFieldPages.setText(null);
        TextAreaDescription.setText(null);
        TextFieldLink.setText(null);
        TextFieldCategories.setText(null);
    }
}

