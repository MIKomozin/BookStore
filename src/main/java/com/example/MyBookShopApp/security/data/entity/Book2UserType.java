package com.example.MyBookShopApp.security.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book2user_type")
public class Book2UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    //поле для подсчета популярности книги (пока так придумал)
    @Column(columnDefinition = "REAL NOT NULL DEFAULT 0")
    private Float point;

    @OneToMany(mappedBy = "type")
    List<Book2User> book2UserList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }
}
