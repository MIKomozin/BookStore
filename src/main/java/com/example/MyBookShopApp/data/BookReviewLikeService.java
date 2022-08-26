package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.DtoRateBookReview;
import com.example.MyBookShopApp.data.entity.BookReviewLike;
import com.example.MyBookShopApp.data.repository.BookReviewLikeRepository;
import com.example.MyBookShopApp.data.repository.BookReviewRepository;
import com.example.MyBookShopApp.security.BookstoreUser;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookReviewLikeService {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookstoreUserRegister bookstoreUserRegister;

    @Autowired
    public BookReviewLikeService(BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository bookReviewRepository, BookstoreUserRegister bookstoreUserRegister) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookstoreUserRegister = bookstoreUserRegister;
    }

    public void addLikeOrDislike(DtoRateBookReview dtoRateBookReview) {
        BookstoreUser bookstoreUser = (BookstoreUser) bookstoreUserRegister.getCurrentUser();
        Integer reviewId = dtoRateBookReview.getReviewid();
        Byte value = dtoRateBookReview.getValue();

        //добавляем лайк/дизлайк в БД
        BookReviewLike bookReviewLike = new BookReviewLike();
        bookReviewLike.setBookReview(bookReviewRepository.findBookReviewById(reviewId));
        bookReviewLike.setUser(bookstoreUser);
        bookReviewLike.setTime(new Date());
        bookReviewLike.setValue(value);
        bookReviewLikeRepository.save(bookReviewLike);
    }
}