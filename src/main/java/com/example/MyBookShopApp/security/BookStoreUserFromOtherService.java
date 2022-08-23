package com.example.MyBookShopApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class BookStoreUserFromOtherService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookStoreUserFromOtherService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    //регистрация нового пользователя при использовании REST API GitHub
    public void registerNewUser(DefaultOAuth2User defaultOAuth2User) {
        BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setName(defaultOAuth2User.getName());
        bookstoreUser.setEmail("укажите свой email");
        bookstoreUser.setPhone("укажите свой номер телефона");
        bookstoreUser.setPassword("");
        bookstoreUserRepository.save(bookstoreUser);
    }

    public BookstoreUser findUserByName(String name) {
        return bookstoreUserRepository.findBookstoreUserByName(name);
    }

//        public void authenticationNewUser(BookstoreUser bookstoreUser) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(bookstoreUser.getName(), bookstoreUser.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

}
