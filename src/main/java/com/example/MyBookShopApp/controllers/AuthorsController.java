package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.entity.Author;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("slug")
    public String slug() {
        return "";
    }

    @ModelAttribute("authorById")
    public Author authorById() {
        return new Author();
    }

    @GetMapping("authors")
    public String listOfAuthors(){
        return "/authors/index";
    }

    @GetMapping("authors/{slug}")
    public String authorPage(@PathVariable(value = "slug", required = false) String slug,
                              Model model){
        model.addAttribute("slug", slug);
        model.addAttribute("authorBySlug", authorService.getAuthorBySlug(slug));
        model.addAttribute("searchResult", authorService.getBooksByAuthorBySlug(slug, 0, 5).getContent());
        model.addAttribute("booksCount", authorService.getNumberOfBooksByAuthorSlug(slug));
        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String booksOfPickAuthor(@PathVariable(value = "slug", required = false) String slug,
                              Model model){
        model.addAttribute("slug", slug);
        model.addAttribute("authorBySlug", authorService.getAuthorBySlug(slug));
        model.addAttribute("searchResult", authorService.getBooksByAuthorBySlug(slug, 0, 10).getContent());
        return "/books/author";
    }

    @GetMapping("/books/author/{slug}/page")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByAuthor(@RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit,
                                                    @PathVariable(value = "slug", required = false) String slug) {
        return new BooksPageDto(authorService.getBooksByAuthorBySlug(slug, offset, limit).getContent());
    }
}
