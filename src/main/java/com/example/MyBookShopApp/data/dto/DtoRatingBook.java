package com.example.MyBookShopApp.data.dto;

public class DtoRatingBook {
    private String value;
    private String bookId;

    public Integer getValue() {
        return Integer.parseInt(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getBookId() {
        return Integer.parseInt(bookId);
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
