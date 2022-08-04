package com.example.MyBookShopApp.errs;

public class UserExistException extends Exception {
    public UserExistException(String message) {
        super(message);
    }
}
