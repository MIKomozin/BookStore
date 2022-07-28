package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {

}
