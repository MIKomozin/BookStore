package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.BookReview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookReviewServiceTests {

    private final BookReviewService bookReviewService;

    @Autowired
    public BookReviewServiceTests(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @Test
    void getAllBookReviewsByBookId() {
        //рейтинг нужен для отображения списка отзывов в определенном порядке (по рейтингу)
        List<BookReview> reviewList = bookReviewService.getAllBookReviewsByBookId(5);//для книги "Moebius" id = 5
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


    @Test
    void getRatingReview() {
        //рейтинг отзывов для книги с id = 5 (ожидаемые значения)
        int rev_rating_6 = 5;
        int rev_rating_7 = 8;
        int rev_rating_8 = 3;
        int rev_rating_9 = 0;
        int rev_rating_10 = -2;

        //рейтинг отзывов полученный из расчетов
        int rating_6 = bookReviewService.getRatingReview(6);
        int rating_7 = bookReviewService.getRatingReview(7);
        int rating_8 = bookReviewService.getRatingReview(8);
        int rating_9 = bookReviewService.getRatingReview(9);
        int rating_10 = bookReviewService.getRatingReview(10);

        assertEquals(rev_rating_6, rating_6);
        assertEquals(rev_rating_7, rating_7);
        assertEquals(rev_rating_8, rating_8);
        assertEquals(rev_rating_9, rating_9);
        assertEquals(rev_rating_10, rating_10);
    }
}