package com.example.MyBookShopApp.data.entity;

import com.example.MyBookShopApp.security.data.entity.Book2User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
@ApiModel(description = "entity representing a book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id generated db automatically")
    private Integer id;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    @ApiModelProperty("data of book publication")
    private Date pubDate;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    @ApiModelProperty("if book is bestseller isBestseller = 1, else isBestseller = 0")
    private byte isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty("some characters")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty("book title")
    private String title;

    @ApiModelProperty("image URL")
    private String image;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("book description text")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    @ApiModelProperty("book price without discount")
    @JsonProperty("price")
    private Integer price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    @ApiModelProperty("discount value for book")
    private byte discount;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Tag> book2Tag;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Genre> book2Genres;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Author> book2Authors;

    @JsonProperty
    public Integer discountPrice() {
        Integer discountPrice = price - Math.toIntExact(Math.round(price*discount/100));
        return discountPrice;
    }

    @JsonProperty("authors")
    public String authorsFullName() {
        String authorsFullName = book2Authors.stream()
                .map(Book2Author::getAuthor)
                .map(Author::getName)
                .collect(Collectors.joining("\n", "", ""));
        if (authorsFullName.length()!=0) {
            return authorsFullName;
        } else
            return "";
    }

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookFile> bookFileList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookRating> bookRatingList;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookReview> bookReviewList;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2User> book2UserList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public byte getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(byte isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public byte getDiscount() {
        return discount;
    }

    public void setDiscount(byte discount) {
        this.discount = discount;
    }

    public List<Book2Tag> getBook2Tag() {
        return book2Tag;
    }

    public void setBook2Tag(List<Book2Tag> book2Tag) {
        this.book2Tag = book2Tag;
    }

    public List<Book2Genre> getBook2Genres() {
        return book2Genres;
    }

    public void setBook2Genres(List<Book2Genre> book2Genres) {
        this.book2Genres = book2Genres;
    }

    public List<Book2Author> getBook2Authors() {
        return book2Authors;
    }

    public void setBook2Authors(List<Book2Author> book2Authors) {
        this.book2Authors = book2Authors;
    }

    public List<BookFile> getBookFileList() {
        return bookFileList;
    }

    public void setBookFileList(List<BookFile> bookFileList) {
        this.bookFileList = bookFileList;
    }

    public List<BookRating> getBookRatingList() {
        return bookRatingList;
    }

    public void setBookRatingList(List<BookRating> bookRatingList) {
        this.bookRatingList = bookRatingList;
    }

    public List<BookReview> getBookReviewList() {
        return bookReviewList;
    }

    public void setBookReviewList(List<BookReview> bookReviewList) {
        this.bookReviewList = bookReviewList;
    }

    public List<Book2User> getBook2UserList() {
        return book2UserList;
    }

    public void setBook2UserList(List<Book2User> book2UserList) {
        this.book2UserList = book2UserList;
    }
}

/*
   //метод для подсчета популярности книги
    public double getPopIndex() {
        double popIndex = 0.0;
        if (book2UserList.isEmpty()) {
            return 0.0;
        } else {
            for (Book2User book2Users : book2UserList) {
                String PCK = book2Users.getType().getName();
                if (PCK.equals("PAID")) {
                    popIndex = popIndex + 1;
                } else if (PCK.equals("CART")) {
                    popIndex = popIndex + 0.7;
                } else if (PCK.equals("KEPT")) {
                    popIndex = popIndex + 0.4;
                } else {
                    continue;
                }
            }
        }
        return popIndex;
    }
 */