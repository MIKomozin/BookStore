package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.BookReview;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository, BookRepository bookRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
    }

    public void addNewReviewIntoDataBase(String text, String bookId) {
        BookReview bookReview = new BookReview();
        bookReview.setBook(bookRepository.findBooksById(Integer.parseInt(bookId)));
        // с users еще не работали, поэтому буду генерировать id рандомно
        Integer userId = Math.toIntExact(Math.round((Math.random() + 1)*10));
        bookReview.setUserId(userId);

        bookReview.setTime(new Date());
        bookReview.setText(text);
        bookReviewRepository.save(bookReview);
    }

    public List<BookReview> getAllBookReviewsByBookId(Integer bookId){
        List <BookReview> bookReviewListSortByRating = bookReviewRepository.findBookReviewsByBookId(bookId).stream()
                .sorted(Comparator.comparingLong(BookReview::getRatingReview).reversed())
                .collect(Collectors.toList());
        return bookReviewListSortByRating;
    }
}
