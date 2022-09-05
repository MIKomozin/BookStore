package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
                bookService.getPageRecentBooks(0, 20).getContent());
        return "/books/recent";
    }

    @GetMapping("/books/recent/page")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("from") String from,
                                          @RequestParam("to") String to,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) {
        Date dateFrom = null;
        Date dateTo = null;
        try {
            dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(from);
        } catch (ParseException parseException1) {
            Logger.getLogger(this.getClass().getSimpleName()).info("dateFrom do not parse, dateFrom is null");
            try {
                dateTo = new SimpleDateFormat("dd.MM.yyyy").parse(to);
            } catch (ParseException parseException2) {
                Logger.getLogger(this.getClass().getSimpleName()).info("dateTo do not parse, dateTo is null");
            }
        }
        return new BooksPageDto(bookService.getPageRecentBooksByPubDateBetween(dateFrom, dateTo, offset, limit).getContent());
    }
}
