package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.data.GenreService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.GenreDto;
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

    @ModelAttribute("genreDto")
    public GenreDto genreDto() {
        return new GenreDto();
    }

    @GetMapping("genres")
    public String genresPage(Model model){
        model.addAttribute("genres", genreService.getGenresData());
        return "/genres/index";
    }

    @GetMapping("genres/slug/{slugInd}")
    public String getSomeBooksByGenre(@PathVariable(value = "slugInd", required = false) GenreDto genreDto,
                                    Model model) {
        model.addAttribute("genreDto", genreDto);
        model.addAttribute("genreByslug", genreService.getGenreBySlug(genreDto.getSlug()));
        model.addAttribute("searchResult", genreService.getBooksByGenreSlug(genreDto.getSlug(), 0, 10).getContent());
        return "/genres/slug";
    }

    @GetMapping("genres/slug/page/{slugInd}")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByGenre(@RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit,
                                                    @PathVariable(value = "slugInd", required = false) GenreDto genreDto) {
        return new BooksPageDto(genreService.getBooksByGenreSlug(genreDto.getSlug(), offset, limit).getContent());
    }

}
