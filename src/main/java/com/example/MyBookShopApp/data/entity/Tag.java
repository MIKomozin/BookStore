package com.example.MyBookShopApp.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name", columnDefinition = "TEXT NOT NULL")
    private String tagName;

    @OneToMany(mappedBy = "tag")
    @JsonIgnore
    List<Book2Tag> tag2Book;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<Book2Tag> getTag2Book() {
        return tag2Book;
    }

    public void setTag2Book(List<Book2Tag> tag2Book) {
        this.tag2Book = tag2Book;
    }
}
