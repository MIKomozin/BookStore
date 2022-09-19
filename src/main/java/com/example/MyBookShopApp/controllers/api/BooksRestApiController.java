package com.example.MyBookShopApp.controllers.api;

import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.errs.BookStoreApiWrongParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api("book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("operation to get books of bookshop by passed author first name")
    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("operation to get books of bookshop by passed author title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String title) throws BookStoreApiWrongParameterException {
        ApiResponse<Book> response = new ApiResponse<>();
        List<Book> data = bookService.getBooksByTitle(title);
        response.setDebugMessage("successful request");
        response.setMessage("data size: " + data.size() + " elements");
        response.setStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("operation to get books from passed min price to max price")
    public ResponseEntity<List<Book>> priceRangeBooks(@RequestParam("min") Integer min, @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("operation to get books with max discount")
    public ResponseEntity<List<Book>> maxDiscountBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("get bestsellers books (which is_bestseller = 1)")
    public ResponseEntity<List<Book>> bestsellersBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

}
