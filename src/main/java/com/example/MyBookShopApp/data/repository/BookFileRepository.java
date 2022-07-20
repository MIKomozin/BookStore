package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    public BookFile getBookFileByHash(String hash);
}
