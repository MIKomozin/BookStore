package com.example.MyBookShopApp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Integer price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    @ApiModelProperty("discount value for book")
    private byte discount;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnore
    private Author author;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
