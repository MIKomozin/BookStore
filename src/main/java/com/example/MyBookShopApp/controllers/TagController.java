package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("tagName")
    public String tagPhrase() {
        return "";
    }

    @GetMapping("/tags/{tagName}")
    public String getSomeBooksByTag(@PathVariable(value = "tagName", required = false) String tagName,
                                  Model model) {
        model.addAttribute("tagName", tagName);
        model.addAttribute("tagByTagName", tagService.getTagByTagName(tagName));
        model.addAttribute("searchResult", tagService.getBooksByTagName(tagName, 0, 10).getContent());

        return "/tags/index";
    }

    @GetMapping("/tags/{tagName}/page")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByTag(@RequestParam("offset") Integer offset,
                                                  @RequestParam("limit") Integer limit,
                                                  @PathVariable(value = "tagName", required = false) String tagName) {
        return new BooksPageDto(tagService.getBooksByTagName(tagName, offset, limit).getContent());
    }
}

