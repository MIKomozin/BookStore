package com.example.MyBookShopApp.data.dto;

public class GenreDto {

    private String slug;

    public GenreDto() {
        //пустой конструктор DTO класса
    }

    public GenreDto(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
