package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, books.description AS description, price, discount, users_buy_book, users_added_book_to_cart, users_postponed_book FROM books " +
            "JOIN book2author ON books.id = book_id " +
            "JOIN authors ON author_id = authors.id " +
            "WHERE name LIKE '%' || ?1 || '%'",
            nativeQuery = true)
    List<Book> findBooksByAuthorNameContaining(String authorName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceBetween(Integer min, Integer max);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books where discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

    //RECENT
    //with pubDate
    @Query(value = "SELECT * FROM books WHERE pub_date >= ?1 AND pub_date <= ?2 ORDER BY pub_date DESC",
            nativeQuery = true, countQuery = "SELECT count(*) FROM books WHERE pub_date >= ?1 AND pub_date <= ?2")
    Page<Book> findBooksByPubDateBetween(Date from, Date to, Pageable nextPage);

    //only with pubDate from
    @Query(value = "SELECT * FROM books WHERE pub_date >= ?1 ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByPubDateAfter(Date from, Pageable nextPage);

    //only with pubDate to
    @Query(value = "SELECT * FROM books WHERE pub_date <= ?1 ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByPubDateBefore(Date to, Pageable nextPage);

    //POPULAR
    //метод изменится при внесении изменений в БД
    @Query(value = "SELECT * FROM books ORDER BY (users_buy_book+0.7*users_added_book_to_cart+0.4*users_postponed_book) DESC",
            nativeQuery = true, countQuery = "SELECT count(*) FROM books")
    Page<Book> findBooksByPopIndex(Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, slug, title, image, description, price, discount, users_buy_book, users_added_book_to_cart, users_postponed_book FROM books JOIN book2tag ON books.id = book_id JOIN tags ON tag_id = tags.id WHERE tag_id = ?1",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book2tag WHERE tag_id = ?1")
    Page<Book> findBooksByTagId(Integer tagId, Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, description, price, discount, users_buy_book, users_added_book_to_cart, users_postponed_book FROM books JOIN book2genre ON books.id = book_id JOIN genre ON genre_id = genre.id WHERE genre.slug = ?1",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book2genre JOIN genre ON genre_id = genre.id WHERE genre.slug = ?1")
    Page<Book> findBooksByGenreSlug(String slugInd, Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, books.description AS description, price, discount, users_buy_book, users_added_book_to_cart, users_postponed_book FROM books JOIN book2author ON books.id = book_id JOIN authors ON author_id = authors.id WHERE author_id = ?1",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book2author WHERE author_id = ?1")
    Page<Book> findBooksByAuthorId(Integer authorId, Pageable nextPage);

    @Query(value = "SELECT count(*) FROM book2author WHERE author_id = ?1", nativeQuery = true)
    Integer findNumberOfBooksByAuthorId(Integer authorId);

    Book findBooksBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    @Query(value = "SELECT SUM (rating_star*number_of_users) FROM book_rating JOIN books ON book_id = books.id WHERE books.slug=?1", nativeQuery = true)
    Integer findSumRatingBookBySlug(String slug);

    @Query(value = "SELECT SUM (number_of_users) FROM book_rating JOIN books ON book_id = books.id WHERE books.slug=?1", nativeQuery = true)
    Integer findNumberOfUsersGiveRatingBookBySlug(String slug);

    @Query(value = "SELECT number_of_users FROM book_rating JOIN books ON book_id = books.id WHERE books.slug=?1 AND rating_star=?2", nativeQuery = true)
    Integer findNumberOfUsersBookBySlugAndByNumberOfStars(String slug, Integer NumberOfStars);

    Book findBooksById(Integer id);

    @Query(value = "SELECT books.id FROM books JOIN book_review ON books.id=book_id WHERE  book_review.id=?1", nativeQuery = true)
    Integer findBookIdByReviewId(Integer reviewId);
}
