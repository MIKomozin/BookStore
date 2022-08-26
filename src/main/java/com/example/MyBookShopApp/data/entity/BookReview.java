package com.example.MyBookShopApp.data.entity;

import com.example.MyBookShopApp.security.BookstoreUser;

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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private BookstoreUser user;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Date time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "bookReview")
    private List<BookReviewLike> bookReviewLikeList;

    //Date to String
    public String getDateByString(){
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formater.format(time);
    }

    //количество лайков у данного отзыва
    public Long getSumLikes() {
        Long count = bookReviewLikeList.stream().filter(v -> v.getValue() == 1).count();
        return count;
    }

    //количество дизлайков у данного отзыва
    public Long getSumDislikes() {
        Long count = bookReviewLikeList.stream().filter(v -> v.getValue() == -1).count();
        return count;
    }

    //рейтинг отзыва = (сумма лайков) - (сумма дизлайков)
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

    public BookstoreUser getUser() {
        return user;
    }

    public void setUser(BookstoreUser user) {
        this.user = user;
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
}
