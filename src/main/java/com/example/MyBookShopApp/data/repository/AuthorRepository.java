package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    //3
    @Query(value = "SELECT * FROM authors where id = ?1", nativeQuery = true)
    Author findAuthorById(Integer authorId);
}
