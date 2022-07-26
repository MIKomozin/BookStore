package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    @Query(value = "SELECT * FROM book_rating WHERE rating_star=?1 AND book_id=?2", nativeQuery = true, countQuery = "SELECT count(*) FROM book_rating WHERE rating_star=?1 AND book_id=?2")
    BookRating findBookRatingListByRatingStarAndBookId(Integer ratingStar, Integer bookId);

    @Query(value = "SELECT MAX(id) FROM book_rating", nativeQuery = true)
    Integer findMaxId();


}
