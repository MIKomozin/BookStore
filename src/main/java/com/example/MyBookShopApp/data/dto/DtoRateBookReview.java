package com.example.MyBookShopApp.data.dto;

public class DtoRateBookReview {
    private String value;
    private String reviewid;

    public Byte getValue() {
        return Byte.parseByte(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getReviewid() {
        return Integer.parseInt(reviewid);
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }
}
