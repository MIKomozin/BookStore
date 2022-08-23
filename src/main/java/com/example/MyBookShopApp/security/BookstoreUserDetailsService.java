package com.example.MyBookShopApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findUserByEmail(username);
        if (bookstoreUser == null) {
            Logger.getLogger(UsernameNotFoundException.class.getSimpleName()).info("исключение " + UsernameNotFoundException.class.getSimpleName() + " поймано");
            throw new UsernameNotFoundException("Пользователя c таким email не существует. Введите корректное имя пользователя или зарегистрируйтесь.");
        } else {
            return new BookstoreUserDetails(bookstoreUser);
        }
    }
}
