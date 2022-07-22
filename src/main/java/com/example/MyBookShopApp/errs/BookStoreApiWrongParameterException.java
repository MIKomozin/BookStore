package com.example.MyBookShopApp.errs;

public class BookStoreApiWrongParameterException extends Exception {
    public BookStoreApiWrongParameterException(String message) {
        super(message);
    }
}
