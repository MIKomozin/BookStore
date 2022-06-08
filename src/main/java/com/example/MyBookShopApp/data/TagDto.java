package com.example.MyBookShopApp.data;

public class TagDto {

    private String id;

    public TagDto() {
        //пустой конструктор DTO класса
    }

    public TagDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
