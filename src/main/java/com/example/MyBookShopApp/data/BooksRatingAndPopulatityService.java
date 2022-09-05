package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksRatingAndPopulatityService {

    private final BookRepository bookRepository;

    @Autowired
    public BooksRatingAndPopulatityService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //Метод для поиска популярных книг и их сортировки
    public Page<Book> getMostPopularBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPopularBookAndSort(nextPage);
    }
}