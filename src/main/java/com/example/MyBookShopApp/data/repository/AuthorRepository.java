package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "SELECT * FROM authors WHERE slug = ?1", nativeQuery = true)
    Author findAuthorBySlug(String slug);

    @Query(value = "SELECT * FROM authors ORDER BY name", countQuery = "SELECT count(*) FROM authors", nativeQuery = true)
    List<Author> findSortedListOfAuthors();
}
