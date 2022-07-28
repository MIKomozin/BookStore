package com.example.MyBookShopApp.data.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    /*
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    */

    @Column(columnDefinition = "INT NOT NULL")
    private Integer userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Date time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewLike> bookReviewLikeList;

    public String getDateByString(){
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formater.format(time);
    }

    public Long getSumLikes() {
        Long count = bookReviewLikeList.stream().filter(v -> v.getValue() == 1).count();
        return count;
    }

    public Long getSumDislikes() {
        Long count = bookReviewLikeList.stream().filter(v -> v.getValue() == -1).count();
        return count;
    }

    public Long getRatingReview() {
        return getSumLikes() - getSumDislikes();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<BookReviewLike> getBookReviewLikeList() {
        return bookReviewLikeList;
    }

    public void setBookReviewLikeList(List<BookReviewLike> bookReviewLikeList) {
        this.bookReviewLikeList = bookReviewLikeList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
