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

    private static ResultSet executeQuery(String query) throws SQLException {
        return getConnection().createStatement().executeQuery(query);
    }

    private static void executeUpdate(String query) throws SQLException {
        getConnection().createStatement().executeUpdate(query);
    }

    public static ResultSet getUser(String login, String password) throws SQLException {
        return executeQuery(String.format
                (
                        "SELECT * FROM users WHERE login = '%s' AND password = '%s'",
                        login,
                        password
                )
        );
    }

    public static ResultSet getUser(String login) throws SQLException {
        return executeQuery(String.format("SELECT * FROM users WHERE login = '%s'", login));
    }

    public static ResultSet getAllBooks() throws SQLException {
        return executeQuery("SELECT * FROM books");
    }

    public static ResultSet getAllLogs() throws SQLException {
        return executeQuery("SELECT * FROM logs");
    }

    public static ResultSet getAllUsers() throws SQLException {
        return executeQuery("SELECT * FROM users");
    }

    public static ResultSet getAllCategories() throws SQLException {
        return executeQuery("SELECT * FROM categories");
    }

    public static ResultSet getBook(int bookId) throws SQLException {
        return executeQuery(String.format("SELECT * FROM books WHERE id = %d", bookId));
    }

    public static ResultSet getBook
            (String title, String author, int year, int pages, String description, String link) throws SQLException {
        return executeQuery(String.format
                ("SELECT * FROM books " +
                                "WHERE title = '%s' AND author = '%s' AND year = '%d' AND " +
                                "pages = '%d' AND description = '%s' AND link = '%s'",
                        title, author, year, pages, description, link));
    }

    public static ResultSet getCategoryByName(String categoryName) throws SQLException {
        return executeQuery(String.format("SELECT id FROM categories WHERE name = '%s'", categoryName));
    }

    public static ResultSet getBookCategories(int bookId) throws SQLException {
        return executeQuery(String.format
                (
                        "SELECT name FROM books_categories " +
                                "INNER JOIN categories on categories.id = books_categories.category_id " +
                                "WHERE book_id = %d",
                        bookId
                )
        );
    }

    public static void createBook(Book book) throws SQLException {
        executeUpdate(String.format
                (
                        "INSERT INTO books (title, author, year, pages, description, link)" +
                                "VALUES ('%s', '%s', '%d', '%d', '%s', '%s')",
                        book.getTitle(), book.getAuthor(), book.getYear(), book.getPages(),
                        book.getDescription(), book.getLink()
                )
        );

        if (book.getCategories() == null) return;

        var newBook = getBook(book.getTitle(), book.getAuthor(), book.getYear(), book.getPages(),
                book.getDescription(), book.getLink());
        newBook.next();

        var categories = book.getCategories().split(", ");

        createBookCategories(newBook.getInt("id"), categories);
    }

    public static void createUser(String login, String password) throws SQLException {
        executeUpdate(String.format("INSERT INTO users (login, password) VALUES ('%s', '%s')", login, password));
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
        executeUpdate(String.format
                (
                        "INSERT INTO books_categories (book_id, category_id) VALUES (%d, %d)",
                        bookId, categoryId
                ));
    }

    public static void createCategory(String categoryName) throws SQLException {
        executeUpdate(String.format("INSERT INTO categories (name) VALUES ('%s')", categoryName));
    }

    public static void updateBook(Book book) throws SQLException {
        executeUpdate(String.format
                (
                        "UPDATE books " +
                                "SET title = '%s', " +
                                "author = '%s', " +
                                "year = '%d', " +
                                "pages = '%d', " +
                                "description = '%s', " +
                                "link = '%s' " +
                                "WHERE id = %d",
                        book.getTitle(), book.getAuthor(), book.getYear(), book.getPages(),
                        book.getDescription(), book.getLink(), book.getId()
                )
        );

        if (book.getCategories() == null) return;

        var categories = book.getCategories().split(", ");

        removeBookCategories(book.getId());

        createBookCategories(book.getId(), categories);
    }

    public static void removeBookCategories(int bookId) throws SQLException {
        executeUpdate(String.format("DELETE FROM books_categories WHERE book_id = %d", bookId));
    }

    public static void removeBook(int bookId) throws SQLException {
        executeUpdate(String.format("DELETE FROM books WHERE id = %d", bookId));
    }
}
