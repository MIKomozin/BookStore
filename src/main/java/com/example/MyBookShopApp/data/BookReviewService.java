package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.DtoPostNewReview;
import com.example.MyBookShopApp.data.entity.BookReview;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookReviewLikeRepository;
import com.example.MyBookShopApp.data.repository.BookReviewRepository;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRepository bookRepository;
    private final BookstoreUserRegister bookstoreUserRegister;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository, BookRepository bookRepository, BookstoreUserRegister bookstoreUserRegister) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRepository = bookRepository;
        this.bookstoreUserRegister = bookstoreUserRegister;
    }

    public void addNewReviewIntoDataBase(DtoPostNewReview dtoPostNewReview) {
        BookstoreUser bookstoreUser = (BookstoreUser) bookstoreUserRegister.getCurrentUser();
        Integer bookId = dtoPostNewReview.getBookId();
        String text = dtoPostNewReview.getText();
        //добавляем отзыв в БД
        BookReview bookReview = new BookReview();
        bookReview.setBook(bookRepository.findBooksById(bookId));
        bookReview.setUser(bookstoreUser);
        bookReview.setTime(new Date());
        bookReview.setText(text);
        bookReviewRepository.save(bookReview);
    }

    public List<BookReview> getAllBookReviewsByBookId(Integer bookId){
        return Objects.requireNonNullElse(bookReviewRepository.findBookReviewsByBookId(bookId), new ArrayList<>());
    }

    public Integer getCountLikesOfReviewById(Integer reviewId) {
        return Objects.requireNonNullElse(bookReviewLikeRepository.findCountLikesReviewById(reviewId), 0);
    }

    public Integer getCountDisLikesOfReviewById(Integer reviewId) {
        return Objects.requireNonNullElse(bookReviewLikeRepository.findCountDisLikesReviewById(reviewId), 0);
    }

    //рейтинг отзыва = (сумма лайков) - (сумма дизлайков)
    public Integer getRatingReview(Integer reviewId) {
        return getCountLikesOfReviewById(reviewId) - getCountDisLikesOfReviewById(reviewId);
    }
}
