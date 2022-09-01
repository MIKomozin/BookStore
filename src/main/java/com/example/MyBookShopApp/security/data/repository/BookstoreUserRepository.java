package com.example.MyBookShopApp.security.data.repository;

import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    BookstoreUser findUserByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE phone = ?1", nativeQuery = true)
    BookstoreUser findUserByPhone(String phone);

}
