package com.example.MyBookShopApp.security.data;

import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findUserByEmailOrPhone(data);
        if (bookstoreUser == null) {
            Logger.getLogger(UsernameNotFoundException.class.getSimpleName()).info("исключение " + UsernameNotFoundException.class.getSimpleName() + " поймано");
            throw new UsernameNotFoundException("Пользователя c такими данными не существует. Введите корректные данные или зарегистрируйтесь.");
        } else {
            return new BookstoreUserDetails(bookstoreUser);
        }
    }
}
