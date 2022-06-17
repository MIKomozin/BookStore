package com.example.MyBookShopApp.data.dto;

public class AuthorDto {

    String id;

    public AuthorDto(String id) {
        this.id = id;
    }

    public AuthorDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIntId() {
        Integer intId = 0;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException a) {
            intId = 1000;
        }
        return intId;
    }
}
