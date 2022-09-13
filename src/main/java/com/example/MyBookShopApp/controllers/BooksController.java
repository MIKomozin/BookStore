package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.data.dto.DtoPostNewReview;
import com.example.MyBookShopApp.data.dto.DtoRateBookReview;
import com.example.MyBookShopApp.data.dto.DtoRatingBook;
import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final BookRatingService bookRatingService;
    private final BookReviewService bookReviewService;
    private final BookReviewLikeService bookReviewLikeService;
    private final BookstoreUserRegister bookstoreUserRegister;

    @Autowired
    public BooksController(BookService bookService,
                           ResourceStorage storage,
                           BookRatingService bookRatingService,
                           BookReviewService bookReviewService,
                           BookReviewLikeService bookReviewLikeService,
                           BookstoreUserRegister bookstoreUserRegister) {
        this.bookService = bookService;
        this.storage = storage;
        this.bookRatingService = bookRatingService;
        this.bookReviewService = bookReviewService;
        this.bookReviewLikeService = bookReviewLikeService;
        this.bookstoreUserRegister = bookstoreUserRegister;
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBooksImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {

        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate); //save new image for book in db

        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash, Model model) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data length: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @ModelAttribute(name = "isAuthenticate")
    public boolean isAuthenticate(){
        return bookstoreUserRegister.userIsAuthenticate();
    }

    //modul7_task2
    @PostMapping("/changeBookStatus")
    public String changeRatingBook(@RequestBody DtoRatingBook dtoRatingBook) {
        bookRatingService.changeDataBaseBookRating(dtoRatingBook);
        String slug = bookService.getBookById(dtoRatingBook.getBookId()).getSlug();
        return "redirect:/books/" + slug;
    }

    //modul7_task3
    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookService.getBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("bookRating", bookService.getRatingBookBySlugForShow(slug));
        model.addAttribute("sumRating", bookService.getSumRatingBookBySlug(slug));
        model.addAttribute("oneStar", bookService.getNumberOfUsersBookBySlugAndByNumberOfStars(slug, 1));
        model.addAttribute("twoStars", bookService.getNumberOfUsersBookBySlugAndByNumberOfStars(slug, 2));
        model.addAttribute("threeStars", bookService.getNumberOfUsersBookBySlugAndByNumberOfStars(slug, 3));
        model.addAttribute("fourStars", bookService.getNumberOfUsersBookBySlugAndByNumberOfStars(slug, 4));
        model.addAttribute("fiveStars", bookService.getNumberOfUsersBookBySlugAndByNumberOfStars(slug, 5));
        model.addAttribute("countReviews", bookService.getNumberOfReviewsForBook(slug));
        model.addAttribute("allReviews", bookReviewService.getAllBookReviewsByBookId(book.getId()));

        return "/books/slug";
    }

    //сделать данные методы только для авторизованных пользователей
    @PostMapping("/bookReview")
    public String postNewReview(@RequestBody DtoPostNewReview dtoPostNewReview) {
        bookReviewService.addNewReviewIntoDataBase(dtoPostNewReview);
        String slug = bookService.getBookById(dtoPostNewReview.getBookId()).getSlug();
        return "redirect:/books/" + slug;
    }

    @PostMapping("/rateBookReview")
    public String rateBookReview(@RequestBody DtoRateBookReview dtoRateBookReview) {
        bookReviewLikeService.addLikeOrDislike(dtoRateBookReview);
        Integer bookId = bookService.getBookIdByReviewId(dtoRateBookReview.getReviewid());
        String slug = bookService.getBookById(bookId).getSlug();
        return "redirect:/books/" + slug;
    }

}