package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query(value = "SELECT * FROM genre WHERE slug = ?1", nativeQuery = true)
    Genre findGenreBySlug(String slugInd);

}
