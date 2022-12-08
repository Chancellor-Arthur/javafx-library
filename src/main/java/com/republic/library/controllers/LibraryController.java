package com.republic.library.controllers;

import com.republic.library.db.DBManager;
import com.republic.library.entities.Book;
import com.republic.library.store.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryController {

    @FXML
    private Button ButtonAddBook;

    @FXML
    private Button ButtonGoToSignIn;

    @FXML
    private Button ButtonGoToAdminPanel;

    @FXML
    private ComboBox<String> ComboBoxCategories;

    @FXML
    private ComboBox<String> ComboBoxAuthors;

    @FXML
    private TableView<Book> TableViewLibrary;

    @FXML
    private TableColumn<Book, String> TableColumnAuthor;

    @FXML
    private TableColumn<Book, Integer> TableColumnPages;

    @FXML
    private TableColumn<Book, String> TableColumnTitle;

    @FXML
    private TableColumn<Book, String> TableColumnCategories;

    @FXML
    private TableColumn<Book, Integer> TableColumnYear;

    @FXML
    void AddBook(ActionEvent event) throws IOException {
        new SceneController().switchToBookCreationScene(event);
    }

    @FXML
    void GoToSignIn(ActionEvent event) throws IOException {
        new SceneController().switchToAuthorizationScene(event);
    }

    @FXML
    void GoToAdminPanel(ActionEvent event) throws IOException {
        new SceneController().switchToAdminPanelScene(event);
    }

    @FXML
    void ShowFilteredBooks(ActionEvent event) throws SQLException {
        if (ComboBoxCategories.getValue() == null)
            showBooks(new ArrayList<>(Collections.singletonList(Store.showAll)), ComboBoxAuthors.getValue());
        else if (ComboBoxAuthors.getValue() == null)
            showBooks(new ArrayList<>(Collections.singletonList(ComboBoxCategories.getValue())), Store.showAll);
        else
            showBooks(new ArrayList<>(Collections.singletonList(ComboBoxCategories.getValue())),
                    ComboBoxAuthors.getValue());
    }

    @FXML
    void ShowBook(MouseEvent event) throws IOException {
        Store.book = TableViewLibrary.getSelectionModel().getSelectedItem();
        new SceneController().switchToBookScene(event);
    }

    @FXML
    public void initialize() throws SQLException {
        var categories = DBManager.getAllCategories();
        List<String> categoryList = new ArrayList<>();
        while (categories.next()) {
            categoryList.add(categories.getString("name"));
        }
        categoryList.add(Store.showAll);

        var books = DBManager.getAllBooks();
        List<String> authorList = new ArrayList<>();
        while (books.next()) {
            authorList.add(books.getString("author"));
        }
        authorList.add(Store.showAll);

        showBooks(categoryList, Store.showAll);

        ComboBoxAuthors.setItems(FXCollections.observableArrayList(authorList));
        ComboBoxCategories.setItems(FXCollections.observableArrayList(categoryList));
        ButtonGoToAdminPanel.setVisible(Store.isAdmin);
    }

    public ObservableList<Book> getBookList(List<String> neededCategories, String neededAuthor) throws SQLException {
        ObservableList<Book> bookList = FXCollections.observableArrayList();

        var books = DBManager.getAllBooks();
        while (books.next()) {
            List<String> categoryList = new ArrayList<String>();

            var categories = DBManager.getBookCategories(books.getInt("id"));
            while (categories.next()) {
                categoryList.add(categories.getString("name"));
            }

            if (neededCategories.stream().noneMatch(categoryList::contains)
                    && !neededCategories.contains(Store.showAll)) continue;
            if (!neededAuthor.equals(books.getString("author"))
                    && !neededAuthor.equals(Store.showAll)) continue;

            StringBuilder categoriesString = new StringBuilder();

            if (!categoryList.isEmpty()) {
                for (var categoryName : categoryList) {
                    categoriesString.append(categoryName).append(", ");
                }
                categoriesString.delete(categoriesString.length() - 2, categoriesString.length());
            }

            var book = new Book
                    (
                            books.getInt("id"),
                            books.getInt("user_id"),
                            books.getString("title"),
                            books.getString("author"),
                            books.getInt("year"),
                            books.getInt("pages"),
                            books.getString("description"),
                            books.getString("link"),
                            categoriesString.toString()
                    );
            bookList.add(book);
        }
        return bookList;
    }

    public void showBooks(List<String> neededCategories, String neededAuthor) throws SQLException {
        ObservableList<Book> list = getBookList(neededCategories, neededAuthor);

        TableColumnTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        TableColumnAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        TableColumnYear.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        TableColumnPages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
        TableColumnCategories.setCellValueFactory(new PropertyValueFactory<Book, String>("categories"));

        TableViewLibrary.setItems(list);
    }

}

