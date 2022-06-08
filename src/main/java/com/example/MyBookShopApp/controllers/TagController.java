package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagController {
    private final BookService bookService;

    @Autowired
    public TagController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("tagPhraseDto")
    public TagDto tagPhraseDto() {
        return new TagDto();
    }

    @GetMapping("/books/tag/{tagId}")
    public String getSomeBooksByTag(@PathVariable(value = "tagId", required = false) TagDto tagDto,
                                  Model model) {
        model.addAttribute("tagPhraseDto", tagDto);
        model.addAttribute("searchResult", bookService.getBooksByTagId(Integer.parseInt(tagDto.getId()), 0, 15).getContent());

        return "/tags/index";
    }

    @GetMapping("/books/tagPage/{tagId}")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByTag(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit,
                                           @PathVariable(value = "tagId", required = false) TagDto tagDto) {
        return new BooksPageDto(bookService.getBooksByTagId(Integer.parseInt(tagDto.getId()), offset, limit).getContent());
    }
}

