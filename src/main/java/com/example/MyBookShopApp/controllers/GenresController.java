package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.data.GenreService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GenresController {

    private final GenreService genreService;

    @Autowired
    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("slug")
    public String slug() {
        return "";
    }

    @GetMapping("genres")
    public String genresPage(Model model){
        model.addAttribute("genres", genreService.getAllGenres());
        return "/genres/index";
    }

    @GetMapping("genres/{slug}")
    public String getSomeBooksByGenre(@PathVariable(value = "slug", required = false) String slug,
                                    Model model) {
        model.addAttribute("slug", slug);
        model.addAttribute("genreByslug", genreService.getGenreBySlug(slug));
        model.addAttribute("searchResult", genreService.getBooksByGenreSlug(slug, 0, 10).getContent());
        return "/genres/slug";
    }

    @GetMapping("genres/{slug}/page")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByGenre(@RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit,
                                                    @PathVariable(value = "slug", required = false) String slug) {
        return new BooksPageDto(genreService.getBooksByGenreSlug(slug, offset, limit).getContent());
    }

}
