package com.example.MyBookShopApp.data.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(name = "reg_time", columnDefinition = "DATE NOT NULL")
    private Date regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer balance;

    @Column(columnDefinition = "VARCHAR(255)")
    private String name;

    /*
    @OneToMany(mappedBy = "user")
    private List<BookReview> bookReviewList;

    @OneToMany(mappedBy = "user")
    private List<BookReviewLike> bookReviewLikeList;
    */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
