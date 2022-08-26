package com.example.MyBookShopApp.data.dto;

public class DtoPostNewReview {
    private String text;
    private String bookId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getBookId() {
        return Integer.parseInt(bookId);
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
