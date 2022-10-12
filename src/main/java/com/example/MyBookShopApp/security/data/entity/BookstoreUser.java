package com.example.MyBookShopApp.security.data.entity;

import com.example.MyBookShopApp.data.entity.BookReview;
import com.example.MyBookShopApp.data.entity.BookReviewLike;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//данный класс будет изменен согласно ТЗ проекта
@Entity
@Table(name = "users")
public class BookstoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(name = "reg_time", columnDefinition = "DATE NOT NULL")
    private Date regTime;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer balance;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    //пока оставим данное поле
    @Column(columnDefinition = "VARCHAR(255)")
    private String email;

    //пока оставим данное поле
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String phone;

    //пока оставим данное поле
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<BookReview> bookReviewList;

    @OneToMany(mappedBy = "user")
    private List<BookReviewLike> bookReviewLikeList;

    @OneToOne(mappedBy = "user")
    private UserContact userContact;

    @OneToMany(mappedBy = "user")
    private List<Book2User> book2UserList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BookReview> getBookReviewList() {
        return bookReviewList;
    }

    public void setBookReviewList(List<BookReview> bookReviewList) {
        this.bookReviewList = bookReviewList;
    }

    public List<BookReviewLike> getBookReviewLikeList() {
        return bookReviewLikeList;
    }

    public void setBookReviewLikeList(List<BookReviewLike> bookReviewLikeList) {
        this.bookReviewLikeList = bookReviewLikeList;
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContact userContact) {
        this.userContact = userContact;
    }

    public List<Book2User> getBook2UserList() {
        return book2UserList;
    }

    public void setBook2UserList(List<Book2User> book2UserList) {
        this.book2UserList = book2UserList;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
