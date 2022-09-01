package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BooksRatingAndPopulatityService {

    private final BookRepository bookRepository;

    @Autowired
    public BooksRatingAndPopulatityService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //METHOD for search popular book
    //метод изменится при внесении изменений в БД
    public Page<Book> getMostPopularBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPopIndex(nextPage);
    }
}
