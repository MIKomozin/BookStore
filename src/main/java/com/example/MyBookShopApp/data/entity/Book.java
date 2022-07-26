package com.example.MyBookShopApp.data.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
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

    //2.2
    @Column(name = "users_buy_book", columnDefinition = "INT NOT NULL DEFAULT 0")
    @ApiModelProperty("number of users bought this book")
    private Integer usersBuyBook;

    @Column(name = "users_added_book_to_cart", columnDefinition = "INT NOT NULL DEFAULT 0")
    @ApiModelProperty("number of users added book to cart")
    private Integer usersAddedBookToCart;

    @Column(name = "users_postponed_book", columnDefinition = "INT NOT NULL DEFAULT 0")
    @ApiModelProperty("number of users postponed book")
    private Integer usersPostponedBook;

    //2.3
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Tag> book2Tag;

    //2.4
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Genre> book2Genres;

    //3
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Book2Author> book2Authors;

    //modul7
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

    //modul7_task2
    @OneToMany(mappedBy = "book")
    private List<BookRating> bookRatings;

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

    public Integer getUsersBuyBook() {
        return usersBuyBook;
    }

    public void setUsersBuyBook(Integer usersBuyBook) {
        this.usersBuyBook = usersBuyBook;
    }

    public Integer getUsersAddedBookToCart() {
        return usersAddedBookToCart;
    }

    public void setUsersAddedBookToCart(Integer usersAddedBookToCart) {
        this.usersAddedBookToCart = usersAddedBookToCart;
    }

    public Integer getUsersPostponedBook() {
        return usersPostponedBook;
    }

    public void setUsersPostponedBook(Integer usersPostponedBook) {
        this.usersPostponedBook = usersPostponedBook;
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

    public List<BookRating> getBookRatings() {
        return bookRatings;
    }

    public void setBookRatings(List<BookRating> bookRatings) {
        this.bookRatings = bookRatings;
    }
}
