package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import com.example.MyBookShopApp.security.data.dto.RegistrationForm;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.repository.BookstoreUserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookstoreUserRegisterTests {

    private final BookstoreUserRegister bookstoreUserRegister;
    private final PasswordEncoder passwordEncoder;

    private RegistrationForm registrationForm;

    @MockBean
    private BookstoreUserRepository bookstoreUserRepositoryMock;

    @Autowired
    public BookstoreUserRegisterTests(BookstoreUserRegister bookstoreUserRegister, PasswordEncoder passwordEncoder) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.passwordEncoder = passwordEncoder;
    }


    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@gmail.com");
        registrationForm.setName("Tester");
        registrationForm.setPhone("89994672290");
        registrationForm.setPassword("iddqd");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() throws UserExistException {
        BookstoreUser user = bookstoreUserRegister.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));

        Mockito.verify(bookstoreUserRepositoryMock, Mockito.times(1))
                .save(Mockito.any(BookstoreUser.class));
    }

    @Test
    void registerNewUserByEmailFail() throws UserExistException {
        //задаем поведение в Mock репозитории книг, что при регистрации такого пользователя,
        //пользователь с таким Email уже существует
        Mockito.doReturn(new BookstoreUser())
                .when(bookstoreUserRepositoryMock)
                .findUserByEmail(registrationForm.getEmail());

        BookstoreUser user = bookstoreUserRegister.registerNewUser(registrationForm);//регистрируем пользователя
        assertNull(user);//проверяем, что при регистрации уже существующего пользователя метод вернет null
    }

    @Test
    void registerNewUserByPhoneFail() throws UserExistException {
        //задаем поведение в Mock репозитории книг, что при регистрации такого пользователя,
        //пользователь с таким телефоном уже существует
        Mockito.doReturn(new BookstoreUser())
                .when(bookstoreUserRepositoryMock)
                .findUserByPhone(registrationForm.getPhone());

        BookstoreUser user = bookstoreUserRegister.registerNewUser(registrationForm);
        assertNull(user);
    }

}