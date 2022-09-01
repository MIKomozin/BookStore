package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.DtoPostNewReview;
import com.example.MyBookShopApp.data.entity.BookReview;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookReviewRepository;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final BookstoreUserRegister bookstoreUserRegister;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository, BookRepository bookRepository, BookstoreUserRegister bookstoreUserRegister) {
        this.bookReviewRepository = bookReviewRepository;
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
        List <BookReview> bookReviewList = bookReviewRepository.findBookReviewsByBookId(bookId);
        if (bookReviewList != null) {
            return bookReviewList.stream()
                    .sorted(Comparator.comparingLong(BookReview::getRatingReview).reversed())
                    .collect(Collectors.toList());
        } return new ArrayList<>();//если нечего сортировать, то возвращаем пустой список
    }
}
