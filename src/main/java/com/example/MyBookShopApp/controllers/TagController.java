package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.TagDto;
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

    @ModelAttribute("tagPhraseDto")
    public TagDto tagPhraseDto() {
        return new TagDto();
    }

    @GetMapping("/books/tag/{tagId}")
    public String getSomeBooksByTag(@PathVariable(value = "tagId", required = false) TagDto tagDto,
                                  Model model) {
        model.addAttribute("tagPhraseDto", tagDto);
        model.addAttribute("tagByTagId", tagService.getTagByTagId(tagDto.getIntId()));
        model.addAttribute("searchResult", tagService.getBooksByTagId(tagDto.getIntId(), 0, 10).getContent());

        return "/tags/index";
    }

    @GetMapping("/books/tag/page/{tagId}")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByTag(@RequestParam("offset") Integer offset,
                                                  @RequestParam("limit") Integer limit,
                                                  @PathVariable(value = "tagId", required = false) TagDto tagDto) {
        return new BooksPageDto(tagService.getBooksByTagId(tagDto.getIntId(), offset, limit).getContent());
    }
}

