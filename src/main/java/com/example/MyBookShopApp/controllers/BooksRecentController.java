package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BooksRecentController {

    BookService bookService;

    @Autowired
    public BooksRecentController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @GetMapping("/books/recent")
    public String getRecentPage(Model model) {
        model.addAttribute("searchResult",
                bookService.getPageRecentBooks(0, 8).getContent());
        return "/books/recent";
    }

    @GetMapping("/books/recent/page")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("from") String from,
                                          @RequestParam("to") String to,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) throws ParseException {
        Date dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
        Date dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
        return new BooksPageDto(bookService.getPageRecentBooksByPubDateBetween(dateFrom, dateTo, offset, limit).getContent());
    }
}

/*
if (from.isEmpty() && to.isEmpty()) {
            return new BooksPageDto(bookService.getPageRecentBooks(offset, limit).getContent());
        } else if (to.isEmpty()) {
            return new BooksPageDto(bookService.getPageRecentBooksByPubDateAfter(dateFrom, offset, limit).getContent());
        } else if (from.isEmpty()) {
            return new BooksPageDto(bookService.getPageRecentBooksByPubDateBefore(dateTo, offset, limit).getContent());
        } else {
 */
