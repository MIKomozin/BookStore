package com.example.MyBookShopApp.data.dto;

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

    public Integer getIntId() {
        Integer intId = 1;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException a) {
            intId = 1000;
        }
        return intId;
    }
}
