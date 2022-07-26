package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.entity.BookRating;
import com.example.MyBookShopApp.data.repository.BookRatingRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BookRatingService {

    private final BookRatingRepository bookRatingRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookRatingService(BookRatingRepository bookRatingRepository, BookRepository bookRepository) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookRepository = bookRepository;
    }

    public void changeDataBaseBookRating(Integer ratingStar, Integer bookId) {
        //строка в БД, где нужно увеличить количество пользователей на 1
        BookRating bookRatingToUpdate = bookRatingRepository.findBookRatingListByRatingStarAndBookId(ratingStar, bookId);
        Logger.getLogger(this.getClass().getSimpleName()).info("bookRatingToUpdate: " + bookRatingToUpdate);
        if (bookRatingToUpdate == null) {
            //если такой строки нет, то создаем новую строку в БД
            BookRating bookRating = new BookRating();
            bookRating.setRatingStar(ratingStar);
            bookRating.setNumberOfUsers(1);
            bookRating.setBook(bookRepository.findBooksById(bookId));
            Logger.getLogger(this.getClass().getSimpleName()).info("id: " + bookRating.getId());
            Logger.getLogger(this.getClass().getSimpleName()).info("rating star: " + bookRating.getRatingStar());
            Logger.getLogger(this.getClass().getSimpleName()).info("NumberOfUsers: " + bookRating.getNumberOfUsers());
            Logger.getLogger(this.getClass().getSimpleName()).info("book_id: " + bookRating.getBook().getId());
            bookRatingRepository.save(bookRating);//и сохраняем ее в БД
        } else {
            bookRatingToUpdate.setNumberOfUsers(bookRatingToUpdate.getNumberOfUsers()+1);//устанавливаем новое значения для пользователей
            bookRatingRepository.save(bookRatingToUpdate);//сохраняем в БД
        }
    }


}
