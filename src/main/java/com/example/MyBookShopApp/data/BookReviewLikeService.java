package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Author;
import com.example.MyBookShopApp.data.entity.BookReview;
import com.example.MyBookShopApp.data.entity.BookReviewLike;
import com.example.MyBookShopApp.data.repository.BookReviewLikeRepository;
import com.example.MyBookShopApp.data.repository.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class BookReviewLikeService {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewLikeService(BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository bookReviewRepository) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookReviewRepository = bookReviewRepository;
    }

    public void addLikeOrDislike(String value, String reviewId) {
        BookReviewLike bookReviewLike = new BookReviewLike();
        bookReviewLike.setBookReview(bookReviewRepository.findBookReviewById(Integer.parseInt(reviewId)));
        // с users еще не работали, поэтому буду генерировать id рандомно
        Integer userId = Math.toIntExact(Math.round((Math.random() + 1)*10));
        bookReviewLike.setUserId(userId);

        bookReviewLike.setTime(new Date());
        bookReviewLike.setValue(Byte.parseByte(value));
        bookReviewLikeRepository.save(bookReviewLike);
    }

}
