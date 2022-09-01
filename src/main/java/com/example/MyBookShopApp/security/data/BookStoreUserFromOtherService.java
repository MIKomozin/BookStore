package com.example.MyBookShopApp.security.data;

import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookStoreUserFromOtherService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookStoreUserFromOtherService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    //регистрация нового пользователя при использовании REST API GitHub
    public BookstoreUser convertNewUserToBookstoreUser(DefaultOAuth2User defaultOAuth2User) {
        BookstoreUser bookstoreUser = new BookstoreUser();
        //вытаскиваем из атрибутов имя и почту для добавления в БД, телефон и пароль пользователь придумает сам
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
            if ((attribute.getKey()).equals("name")) {
                if (attribute.getValue() != null) {
                    String userName = (String) attribute.getValue();
                    bookstoreUser.setName(userName);
                } else {
                    //присвоим пользователю цифровой идентификатор
                    bookstoreUser.setName(defaultOAuth2User.getName());
                }
            }
            if ((attribute.getKey()).equals("email")) {
                if (attribute.getValue() != null) {
                    String userEmail = (String) attribute.getValue();
                    bookstoreUser.setEmail(userEmail);
                } else {
                    //присвоим пользователю цифровой идентификатор
                    bookstoreUser.setEmail(defaultOAuth2User.getName());
                }
            }
        }
        //пароль и телефон пустая строка
        bookstoreUser.setPhone("");
        bookstoreUser.setPassword("");//пока с паролем так
        return bookstoreUser;
    }

    public void registerNewUser(BookstoreUser bookstoreUser) throws UserExistException {
        //проверка на уже существующего пользователя с таким же адресом
        if (bookstoreUserRepository.findUserByEmail(bookstoreUser.getEmail()) != null) {
            throw new UserExistException("Пользователь с таким email уже существует");
        } else {
            bookstoreUserRepository.save(bookstoreUser);
        }
    }

//    public void authenticationNewUser(BookstoreUser bookstoreUser) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(bookstoreUser.getEmail(), bookstoreUser.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

}
