package com.republic.library.entities;

public final class Book {
    private final int id;
    private final int userId;
    private final String title;
    private final String author;
    private final int year;
    private final int pages;
    private final String description;
    private final String link;
    private final String categories;

    public Book
            (
                    int id,
                    int userId,
                    String title,
                    String author,
                    int year,
                    int pages,
                    String description,
                    String link,
                    String categories
            ) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.description = description;
        this.link = link;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public int getPages() {
        return pages;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCategories() {
        return categories;
    }
}
