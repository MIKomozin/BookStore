package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "SELECT * FROM tags WHERE id = ?1", nativeQuery = true)
    Tag findTagByTagId(Integer tagId);

}
