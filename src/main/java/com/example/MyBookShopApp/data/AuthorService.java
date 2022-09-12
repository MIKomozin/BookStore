package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Author;
import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.repository.AuthorRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = authorRepository.findSortedListOfAuthors();
        return authors.stream()
                .collect(Collectors.groupingBy((Author a) -> a.getName().substring(0, 1)));
    }

    public Author getAuthorBySlug(String slug) {
        return authorRepository.findAuthorBySlug(slug);
    }

    public Page<Book> getBooksByAuthorBySlug(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByAuthorSlug(slug, nextPage);
    }

    public Integer getNumberOfBooksByAuthorSlug(String slug) {
        return bookRepository.findNumberOfBooksByAuthorSlug(slug);
    }
}
