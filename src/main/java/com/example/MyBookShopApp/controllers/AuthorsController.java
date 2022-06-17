package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.dto.AuthorDto;
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

    @ModelAttribute("authorDto")
    public AuthorDto authorDto() {
        return new AuthorDto();
    }

    @ModelAttribute("authorById")
    public Author authorById() {
        return new Author();
    }

    @GetMapping("authors")
    public String listOfAuthors(){
        return "/authors/index";
    }

    @GetMapping("authors/slug/{authorId}")
    public String authorPage(@PathVariable(value = "authorId", required = false) AuthorDto authorDto,
                              Model model){
        model.addAttribute("authorDto", authorDto);
        model.addAttribute("authorById", authorService.getAuthorById(authorDto.getIntId()));
        model.addAttribute("searchResult", authorService.getBooksByAuthorId(authorDto.getIntId(), 0, 5).getContent());
        model.addAttribute("booksCount", authorService.getNumberOfBooksByAuthorId(authorDto.getIntId()));
        return "/authors/slug";
    }

    @GetMapping("/books/author/slug/{authorId}")
    public String booksOfPickAuthor(@PathVariable(value = "authorId", required = false) AuthorDto authorDto,
                              Model model){
        model.addAttribute("authorDto", authorDto);
        model.addAttribute("authorById", authorService.getAuthorById(authorDto.getIntId()));
        model.addAttribute("searchResult", authorService.getBooksByAuthorId(authorDto.getIntId(), 0, 10).getContent());
        return "/books/author";
    }

    @GetMapping("/books/author/slug/page/{authorId}")
    @ResponseBody
    public BooksPageDto getNextPageSomeBooksByAuthor(@RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit,
                                                    @PathVariable(value = "authorId", required = false) AuthorDto authorDto) {
        return new BooksPageDto(authorService.getBooksByAuthorId(authorDto.getIntId(), offset, limit).getContent());
    }
}
