package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private final BookService bookService;

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWord")
    public String searchWord() {
        return "";
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    /*
    @ModelAttribute("numberOfsearchBook")
    public int numberOfsearchBook() {
        return 5;
    }

    model.addAttribute("numberOfsearchBook", bookService.getBooksByTitle(searchWordDto.getExample()).size());

    без данной модели неправильно считает количество книг.
    Но когда ее добавляю, после повторного нажатия по поиску вылетает ошибка???
    Сколько в проектк багов!!!
    */

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) String searchWord,
                                  Model model) throws EmptySearchException {
        if (searchWord != null) {
            model.addAttribute("searchWord", searchWord);
            model.addAttribute("searchResult",
                    bookService.getPageOfSearchResultBooks(searchWord, 0, 5).getContent());
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по пустой строке невозможен");
        }
    }

    @GetMapping("/search/{searchWord}/page")
    @ResponseBody
    public BooksPageDto getNextSearchPage1(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) String searchWord) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWord, offset, limit).getContent());
    }
}
