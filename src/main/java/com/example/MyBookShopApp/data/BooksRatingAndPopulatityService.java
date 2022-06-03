package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class BooksRatingAndPopulatityService {

    private BookRepository bookRepository;

    @Autowired
    public BooksRatingAndPopulatityService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //METHOD for search popular book
    public Page<Book> getMostPopularBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPopIndex(nextPage);
    }
}
