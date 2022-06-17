package com.example.MyBookShopApp.data.entity;

import java.io.Serializable;

public class Book2AuthorId implements Serializable {

    private int author;
    private int book;

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }
}
