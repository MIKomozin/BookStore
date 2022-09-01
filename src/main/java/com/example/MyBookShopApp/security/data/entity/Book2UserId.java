package com.example.MyBookShopApp.security.data.entity;


import java.io.Serializable;

public class Book2UserId implements Serializable {

    private int user;
    private int book;
    private int type;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
