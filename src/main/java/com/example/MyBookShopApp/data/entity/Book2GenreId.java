package com.example.MyBookShopApp.data.entity;

import java.io.Serializable;

public class Book2GenreId implements Serializable {

    private int genre;
    private int book;

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }
}
