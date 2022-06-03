package com.example.MyBookShopApp.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
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

    Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

    //RECENT
    //with pubDate
    @Query(value = "SELECT * FROM books WHERE pub_date >= ?1 AND pub_date <= ?2 ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByPubDateBetween(Date from, Date to, Pageable nextPage);

    //only with pubDate from
    @Query(value = "SELECT * FROM books WHERE pub_date >= ?1 ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByPubDateAfter(Date from, Pageable nextPage);

    //only with pubDate to
    @Query(value = "SELECT * FROM books WHERE pub_date <= ?1 ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByPubDateBefore(Date to, Pageable nextPage);


    //POPULAR
    @Query(value = "SELECT * FROM books ORDER BY (users_buy_book+0.7*users_added_book_to_cart+0.4*users_postponed_book) DESC", nativeQuery = true)
    Page<Book> findBooksByPopIndex(Pageable nextPage);
}
