package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.BooksRatingAndPopulatityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BooksPopularController {

    private final BooksRatingAndPopulatityService booksRatingAndPopulatityService;

    @Autowired
    public BooksPopularController(BooksRatingAndPopulatityService booksRatingAndPopulatityService) {
        this.booksRatingAndPopulatityService = booksRatingAndPopulatityService;
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @GetMapping("/books/popular")
    public String getPopularPage(Model model) {
        model.addAttribute("searchResult",
                booksRatingAndPopulatityService.getMostPopularBook(0, 20).getContent());
        return "/books/popular";
    }

    @GetMapping("/books/popular/page")
    @ResponseBody
    public BooksPageDto getNextPopularPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) {
        return new BooksPageDto(booksRatingAndPopulatityService.getMostPopularBook(offset, limit).getContent());
    }

}
