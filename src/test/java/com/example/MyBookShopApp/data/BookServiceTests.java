package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTests {

    private final BookService bookService;

    @Autowired
    public BookServiceTests(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    void getPagePopularBooks() {
        List<Book> popList = bookService.getPagePopularBooks(0, 20).getContent();
        assertNotNull(popList);
        assertFalse(popList.isEmpty());

        //проверяем что списко сформирован по убыванию индекса популярности
        Double popindex;//текущее значение индекса популярности
        Double popindex_pre = null;//предыдущее значение индекса популярности
        for (Book book : popList) {
            //условие для первой книги
            if (popindex_pre == null) {
                popindex_pre = bookService.getPopIndex(book.getId());
                continue;
            }
            //начиная со второй книги находим ее индекс популярности
            popindex = bookService.getPopIndex(book.getId());
            //и сравниваем с предыдущим
            assertTrue(popindex_pre >= popindex);

            //присваиваем предыдущему индексу текущее значение, т.к. в следующей итерации оно будет предыдущим
            popindex_pre = popindex;
        }
    }

    @Test
    void getPopIndex() {
        //индекс популярности некоторых книг по id (ожидаемые значения)
        double book_12_check = 3.4;
        double book_11_check = 2.8;
        double book_7_check = 1.5;
        double book_2_check = 1.4;
        double book_1_check = 0.8;

        //проверка что метод подсчета индекса популярности работает правильно (в программе даный метод вообще не нужен)
        double book_12 = bookService.getPopIndex(12);
        double book_11 = bookService.getPopIndex(11);
        double book_7 = bookService.getPopIndex(7);
        double book_2 = bookService.getPopIndex(2);
        double book_1 = bookService.getPopIndex(1);

        assertEquals(book_12_check, book_12, 0.1);
        assertEquals(book_11_check, book_11, 0.1);
        assertEquals(book_7_check, book_7, 0.1);
        assertEquals(book_2_check, book_2, 0.1);
        assertEquals(book_1_check, book_1, 0.1);
    }

    @Test
    void getPageRecommendedBooks() {
        List<Book> recommendedList = bookService.getPageRecommendedBooks(0, 20).getContent();
        assertNotNull(recommendedList);
        assertFalse(recommendedList.isEmpty());

        //проверяем, что книги в данном списке расположены по убыванию рейтинга
        Double rating;//текущее значение рейтинга книги
        Double rating_pre = null;//предыдущее значение рейтинга книги
        for (Book book : recommendedList) {
            //условие для первой книги
            if (rating_pre == null) {
                rating_pre= bookService.getRatingBookBySlug(book.getSlug());
                continue;
            }
            //начиная со второй книги находим ее рейтинг
            rating = bookService.getRatingBookBySlug(book.getSlug());
            //и сравниваем с предыдущим
            assertTrue(rating_pre >= rating);

            //присваиваем предыдущему значению рейтинга, т.к. в следующей итерации оно будет предыдущим
            rating_pre = rating;
        }
    }

    @Test
    void getRatingBookBySlug() {
        //индекс популярности некоторых книг по id (ожидаемые значения)
        double book_3_check = 4.89;
        double book_5_check = 4.50;
        double book_2_check = 3.25;
        double book_8_check = 2.56;
        double book_23_check = 2.33;

        //подсчет gitрейтинга
        double book_3 = bookService.getRatingBookBySlug(bookService.getBookById(3).getSlug());
        double book_5 = bookService.getRatingBookBySlug(bookService.getBookById(5).getSlug());
        double book_2 = bookService.getRatingBookBySlug(bookService.getBookById(2).getSlug());
        double book_8 = bookService.getRatingBookBySlug(bookService.getBookById(8).getSlug());
        double book_23 = bookService.getRatingBookBySlug(bookService.getBookById(23).getSlug());

        assertEquals(book_3_check, book_3, 0.01);
        assertEquals(book_5_check, book_5, 0.01);
        assertEquals(book_2_check, book_2, 0.01);
        assertEquals(book_8_check, book_8, 0.01);
        assertEquals(book_23_check, book_23, 0.01);
    }


}