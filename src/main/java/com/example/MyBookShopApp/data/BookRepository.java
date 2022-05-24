package com.example.MyBookShopApp.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    //NEW BOOK REST REPOSITORY
    List<Book> findBooksByAuthorNameContaining(String authorFullName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceBetween(Integer min, Integer max);

    List<Book> findBooksByPriceIs(Integer price);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books where discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    //Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

}
