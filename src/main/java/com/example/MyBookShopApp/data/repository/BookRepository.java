package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, books.description AS description, price, discount FROM books " +
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

    //without pubDate
    @Query(value = "SELECT * FROM books ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> findAllBooksAndSortByPubDate(Pageable nextPage);

    //POPULAR
    @Query(value = "WITH sort_book_by_pop AS " +
            "(SELECT books.id AS id, pub_date, is_bestseller, slug, title, image, description, price, discount, SUM(point) AS pop_index FROM books " +
            "LEFT JOIN book2user ON books.id = book_id " +
            "LEFT JOIN book2user_type ON book2user_type.id = type_id " +
            "GROUP BY (books.id) " +
            "ORDER BY pop_index DESC NULLS LAST) " +
            "SELECT id, pub_date, is_bestseller, slug, title, image, description, price, discount FROM sort_book_by_pop",
            countQuery = "SELECT count(*) FROM books",
            nativeQuery = true)
    Page<Book> findPopularBookAndSort(Pageable nextPage);

    @Query(value = "SELECT SUM(point) AS pop_index FROM books " +
            "JOIN book2user ON books.id = book_id " +
            "JOIN book2user_type ON book2user_type.id = type_id " +
            "WHERE books.id = ?1",
            nativeQuery = true)
    Double findPopIndex (Integer id);

    //RECOMMENDED
    @Query(value = "WITH sort_book_by_rating AS " +
            "(SELECT books.id AS id, pub_date, is_bestseller, slug, title, image, description, price, discount, " +
            " round(SUM((rating_star*1.0) * number_of_users)/SUM(number_of_users), 2) AS rating FROM books " +
            " LEFT JOIN book_rating ON books.id = book_id " +
            " GROUP BY (books.id) " +
            " ORDER BY rating DESC NULLS LAST) " +
            "SELECT id, pub_date, is_bestseller, slug, title, image, description, price, discount FROM sort_book_by_rating",
            countQuery = "SELECT count(*) FROM books",
            nativeQuery = true)
    Page<Book> findRecommendedBookAndSort(Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, slug, title, image, description, price, discount FROM books JOIN book2tag ON books.id = book_id JOIN tags ON tag_id = tags.id WHERE tag_id = ?1",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book2tag WHERE tag_id = ?1")
    Page<Book> findBooksByTagId(Integer tagId, Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, description, price, discount FROM books JOIN book2genre ON books.id = book_id JOIN genre ON genre_id = genre.id WHERE genre.slug = ?1",
            nativeQuery = true, countQuery = "SELECT count(*) FROM book2genre JOIN genre ON genre_id = genre.id WHERE genre.slug = ?1")
    Page<Book> findBooksByGenreSlug(String slugInd, Pageable nextPage);

    @Query(value = "SELECT books.id AS id, pub_date, is_bestseller, books.slug AS slug, title, image, books.description AS description, price, discount FROM books JOIN book2author ON books.id = book_id JOIN authors ON author_id = authors.id WHERE author_id = ?1",
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
