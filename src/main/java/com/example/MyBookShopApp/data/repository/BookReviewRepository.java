package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {
//"SELECT book_review.id AS id, text, book_review.time AS time, book_review.user_id AS user_id, book_id FROM book_review JOIN book_review_like ON book_review.id=review_id WHERE book_id=?1 GROUP BY review_id ORDER BY SUM(value)"
   //"SELECT * FROM book_review WHERE book_id=?1"
    @Query(value = "SELECT * FROM book_review WHERE book_id=?1", nativeQuery = true, countQuery = "SELECT count(*) FROM book_review WHERE book_id=?1")
    List<BookReview> findBookReviewsByBookId(Integer bookId);

    BookReview findBookReviewById(Integer reviewId);
}
