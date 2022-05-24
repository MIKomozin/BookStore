package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class BooksPopularController {

    private final BookService bookService;

    @Autowired
    public BooksPopularController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks(){
        return bookService.getBookData();
    }

    @GetMapping("/books/popular")
    public String recentPage(){
        return "/books/popular";
    }
}
