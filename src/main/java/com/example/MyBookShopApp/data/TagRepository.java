package com.example.MyBookShopApp.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}

/*
@Query(value = "SELECT books.id AS id, pub_date, is_bestseller, slug, title, image, description, price, discount, author_id, users_buy_book, users_added_book_to_cart, users_postponed_book FROM books " +
            "JOIN book2tag ON books.id = book_id JOIN tags ON tag_id = tags.id WHERE tag_name = ?1", nativeQuery = true)
    Page<Book> findBooksByTagName(String tagName, Pageable nextPage);

    @Query(value = "SELECT * FROM books WHERE id = ?1", nativeQuery = true)
    Page<Book> findBooksById(List<Integer> bookId, Pageable nextPage);

    @Query(value = "SELECT * FROM book2tag WHERE tag_id = ?1", nativeQuery = true)
    List<Book2Tag> findBookIdByTagId(Integer tagId);

        //2.3
    @Query(value = "SELECT * FROM tags WHERE tag_name = ?1", nativeQuery = true)
    Tag findTagByTagName(String tagName);
*/