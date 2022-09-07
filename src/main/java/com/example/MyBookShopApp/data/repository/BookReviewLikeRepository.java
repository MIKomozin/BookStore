package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {

    @Query(value = "SELECT count(*) FROM book_review_like WHERE value = 1 AND review_id=?1",
            nativeQuery = true)
    Integer findCountLikesReviewById(Integer reviewId);

    @Query(value = "SELECT count(*) FROM book_review_like WHERE value = -1 AND review_id=?1",
            nativeQuery = true)
    Integer findCountDisLikesReviewById(Integer reviewId);
}
