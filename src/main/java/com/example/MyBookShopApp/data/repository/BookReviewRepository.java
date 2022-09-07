package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    @Query(value = "WITH sort_review AS " +
            "(SELECT book_review.id AS id, book_id, book_review.user_id AS user_id, book_review.time AS time, text, " +
            "SUM(value) AS rating_review FROM book_review " +
            "LEFT JOIN book_review_like ON book_review.id = review_id " +
            "WHERE book_id=?1 " +
            "GROUP BY (book_review.id) " +
            "ORDER BY rating_review DESC NULLS LAST) " +
            "SELECT id, book_id, user_id, time, text FROM sort_review",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book_review WHERE book_id=?1")
    List<BookReview> findBookReviewsByBookId(Integer bookId);

    BookReview findBookReviewById(Integer reviewId);
}
