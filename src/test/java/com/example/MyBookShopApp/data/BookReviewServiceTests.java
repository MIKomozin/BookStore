package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.BookReview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewServiceTests {

    private final BookReviewService bookReviewService;

    @Autowired
    public BookReviewServiceTests(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @Test
    void getAllBookReviewsByBookId() {
        //рейтинг нужен для отображения списка отзывов в определенном порядке (по рейтингу)
        List<BookReview> reviewList = bookReviewService.getAllBookReviewsByBookId(3);
        assertNotNull(reviewList);
        assertFalse(reviewList.isEmpty());

        //проверяем что список отзывов сформирован по убыванию рейтинга
        Integer rev_rating;//текущее значение рейтинга отзыва
        Integer rev_rating_pre = null;//предыдущее значение рейтинга отзыва
        for (BookReview bookReview : reviewList) {
            //условие для первого отзыва из списка
            if (rev_rating_pre == null) {
                rev_rating_pre = bookReviewService.getRatingReview(bookReview.getId());
                continue;
            }
            rev_rating = bookReviewService.getRatingReview(bookReview.getId());
            assertTrue(rev_rating_pre >= rev_rating);
            rev_rating_pre = rev_rating;
        }
    }
}