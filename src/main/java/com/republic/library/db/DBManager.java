package com.republic.library.db;

import com.republic.library.entities.Book;

import java.sql.*;

public final class DBManager {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/library";
    static final String DB_USERNAME = "postgres";
    static final String DB_PASSWORD = "009087";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    private static void executeUpdate(String query) throws SQLException {
        getConnection().createStatement().executeUpdate(query);
    }

    public static ResultSet getUser(String login, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE login = ? AND password = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, login);
        statement.setString(2, password);

        return statement.executeQuery();
    }

    public static ResultSet getUser(String login) throws SQLException {
        String query = "SELECT * FROM users WHERE login = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, login);

        return statement.executeQuery();
    }

    public static ResultSet getAllBooks() throws SQLException {
        String query = "SELECT * FROM books";

        PreparedStatement statement = getConnection().prepareStatement(query);
        return statement.executeQuery();

    }

    public static ResultSet getAllLogs() throws SQLException {
        String query = "SELECT * FROM logs";

        PreparedStatement statement = getConnection().prepareStatement(query);

        return statement.executeQuery();
    }

    public static ResultSet getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";

        PreparedStatement statement = getConnection().prepareStatement(query);

        return statement.executeQuery();
    }

    public static ResultSet getAllCategories() throws SQLException {
        String query = "SELECT * FROM categories";

        PreparedStatement statement = getConnection().prepareStatement(query);

        return statement.executeQuery();
    }

    public static ResultSet getBook(int bookId) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, bookId);

        return statement.executeQuery();
    }

    public static ResultSet getBook(String title, String author, int year, int pages, String description, String link) throws SQLException {

        String query = "SELECT * FROM books WHERE title = ? AND author = ? AND year = ? " +
                "AND pages = ? AND description = ? AND link = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, title);
        statement.setString(2, author);
        statement.setInt(3, year);
        statement.setInt(4, pages);
        statement.setString(5, description);
        statement.setString(6, link);

        return statement.executeQuery();
    }

    public static ResultSet getCategoryByName(String categoryName) throws SQLException {
        String query = "SELECT id FROM categories WHERE name = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, categoryName);

        return statement.executeQuery();
    }

    public static ResultSet getBookCategories(int bookId) throws SQLException {
        String query = "SELECT name FROM books_categories INNER JOIN categories " +
                "ON categories.id = books_categories.category_id WHERE book_id = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, bookId);

        return statement.executeQuery();
    }

    public static void createBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, year, pages, description, link) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setInt(3, book.getYear());
        statement.setInt(4, book.getPages());
        statement.setString(5,  book.getDescription());
        statement.setString(6, book.getLink());

        statement.executeUpdate();

        if (book.getCategories() == null) return;

        var newBook = getBook(book.getTitle(), book.getAuthor(), book.getYear(), book.getPages(), book.getDescription(), book.getLink());
        newBook.next();

        var categories = book.getCategories().split(", ");

        createBookCategories(newBook.getInt("id"), categories);
    }

    public static void createUser(String login, String password) throws SQLException {
        String query = "INSERT INTO users (login, password) VALUES (?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, login);
        statement.setString(2, password);

        statement.executeUpdate();
    }

    public static void createBookCategories(int bookId, String[] categories) throws SQLException {
        for (var categoryName : categories) {
            var category = getCategoryByName(categoryName);

            if (category.next()) {
                createBookCategory(bookId, category.getInt("id"));
            } else {
                createCategory(categoryName);

                var newCategory = getCategoryByName(categoryName);
                newCategory.next();

                createBookCategory(bookId, newCategory.getInt("id"));
            }
        }
    }

    public static void createBookCategory(int bookId, int categoryId) throws SQLException {
        String query = "INSERT INTO books_categories (book_id, category_id) VALUES (?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, bookId);
        statement.setInt(2, categoryId);

        statement.executeUpdate();
    }

    public static void createCategory(String categoryName) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, categoryName);

        statement.executeUpdate();
    }

    public static void updateBook(Book book) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ?, year = ?, pages = ?, description = ?, link = ? WHERE id = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setInt(3, book.getYear());
        statement.setInt(4, book.getPages());
        statement.setString(5, book.getDescription());
        statement.setString(6, book.getLink());

        statement.executeUpdate();

        if (book.getCategories() == null) return;

        var categories = book.getCategories().split(", ");

        removeBookCategories(book.getId());

        createBookCategories(book.getId(), categories);
    }

    public static void removeBookCategories(int bookId) throws SQLException {
        String query = "DELETE FROM books_categories WHERE book_id = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, bookId);

        statement.executeQuery();
    }

    public static void removeBook(int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE book_id = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, bookId);

        statement.executeQuery();
    }
}
