package com.example.MyBookShopApp.data.repository;


import com.example.MyBookShopApp.data.entity.Book2Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Book2TagRepository extends JpaRepository<Book2Tag, Integer> {

}

