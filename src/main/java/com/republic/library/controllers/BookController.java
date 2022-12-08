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

public class BookController {

    @FXML
    private Button ButtonGoToLibrary;

    @FXML
    private Button ButtonRemoveBook;

    @FXML
    private Button ButtonUpdateBook;

    @FXML
    private TextField TextFieldAuthor;

    @FXML
    private TextArea TextAreaDescription;

    @FXML
    private TextField TextFieldLink;

    @FXML
    private TextField TextFieldPages;

    @FXML
    private TextField TextFieldTitle;

    @FXML
    private TextField TextFieldCategories;

    @FXML
    private TextField TextFieldYear;

    @FXML
    public void initialize() {
        var book = Store.book;
        var isAdmin = Store.isAdmin;

        if (book != null) fillFields(book);
        setEditPermission(isAdmin);

        ButtonUpdateBook.setVisible(isAdmin);
        ButtonRemoveBook.setVisible(isAdmin);
    }

    @FXML
    void RemoveBook(ActionEvent event) throws SQLException, IOException {
        DBManager.removeBook(Store.book.getId());
        new SceneController().switchToLibraryScene(event);
        Alerts.showSuccessAlert("Книга успешно удалена!");
    }

    @FXML
    void UpdateBook(ActionEvent event) throws SQLException {
        var categories = Utils.checkAndReturnCategories(TextFieldCategories.getText());

        if (!Utils.isValidURL(TextFieldLink.getText().trim())) {
            Alerts.showErrorAlert("Недействительный URL!");
            return;
        }

        if (Integer.parseInt(TextFieldYear.getText().trim()) < 0) {
            Alerts.showErrorAlert("Невозможное количество страниц!");
            return;
        }

        if (Integer.parseInt(TextFieldPages.getText().trim()) <= 0) {
            Alerts.showErrorAlert("Невозможное количество страниц!");
            return;
        }

        var book = new Book
                (
                        Store.book.getId(),
                        Store.book.getUserId(),
                        TextFieldTitle.getText().trim(),
                        TextFieldAuthor.getText().trim(),
                        Integer.parseInt(TextFieldYear.getText().trim()),
                        Integer.parseInt(TextFieldPages.getText().trim()),
                        TextAreaDescription.getText().trim(),
                        TextFieldLink.getText().trim(),
                        categories
                );
        DBManager.updateBook(book);
        Store.book = book;
        fillFields(book);
        Alerts.showSuccessAlert("Книга успешно обновлена!");
    }

    void fillFields(Book book) {
        TextFieldTitle.setText(book.getTitle());
        TextFieldAuthor.setText(book.getAuthor());
        TextFieldYear.setText(String.valueOf(book.getYear()));
        TextFieldPages.setText(String.valueOf(book.getPages()));
        TextAreaDescription.setText(book.getDescription());
        TextFieldLink.setText(book.getLink());
        TextFieldCategories.setText(book.getCategories());
    }

    void setEditPermission(Boolean isAdmin) {
        TextFieldTitle.editableProperty().setValue(isAdmin);
        TextFieldAuthor.editableProperty().setValue(isAdmin);
        TextFieldYear.editableProperty().setValue(isAdmin);
        TextFieldPages.editableProperty().setValue(isAdmin);
        TextAreaDescription.editableProperty().setValue(isAdmin);
        TextFieldLink.editableProperty().setValue(isAdmin);
        TextFieldCategories.editableProperty().setValue(isAdmin);
    }

    @FXML
    void GoToLibrary(ActionEvent event) throws IOException {
        new SceneController().switchToLibraryScene(event);
    }
}
