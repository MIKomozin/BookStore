package com.example.MyBookShopApp.security.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book2user_type")
public class Book2UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "type")
    List<Book2User> book2UserList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book2User> getBook2UserList() {
        return book2UserList;
    }

    public void setBook2UserList(List<Book2User> book2UserList) {
        this.book2UserList = book2UserList;
    }
}
