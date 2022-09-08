package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.data.dto.RegistrationForm;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.jwt.JWTUtil;
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
    private final JWTUtil jwtUtil;

    private RegistrationForm registrationForm;
    private ContactConfirmationPayload confirmationPayload;
    private BookstoreUser user;

    @MockBean
    private BookstoreUserRepository bookstoreUserRepositoryMock;

    @Autowired
    public BookstoreUserRegisterTests(BookstoreUserRegister bookstoreUserRegister, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@gmail.com");
        registrationForm.setName("Tester");
        registrationForm.setPhone("89994672290");
        registrationForm.setPassword("iddqd");

        confirmationPayload = new ContactConfirmationPayload();
        confirmationPayload.setContact("m.i.komozin@gmail.com");
        confirmationPayload.setCode("12345678");

        user = new BookstoreUser();
        user.setPassword("$2a$10$/UzpCCPv.63ZYMg3FYtaG.uzlDkX96Q05MrW.O.3QZL10B3PXjm2S");
        user.setPhone("+7 (222) 222-22-22");
        user.setEmail("m.i.komozin@gmail.com");
        user.setName("Max");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
        confirmationPayload = null;
        user = null;
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

    //тестирование авторизации
    @Test
    void login() {
        Mockito.doReturn(user)
                .when(bookstoreUserRepositoryMock)
                .findUserByEmailOrPhone(confirmationPayload.getContact());

        ContactConfirmationResponse response = bookstoreUserRegister.login(confirmationPayload);
        String answer = response.getResult();
        assertNotNull(answer);
        assertTrue(CoreMatchers.is(answer).matches("true"));

    }

    //jwtToken
    @Test
    void jwtlogin() {
        //задаем поведение в Mock репозитории книг, что при авторизации такого пользователя,
        //пользователь с таким email существует
        Mockito.doReturn(user)
                .when(bookstoreUserRepositoryMock)
                .findUserByEmailOrPhone(confirmationPayload.getContact());

        ContactConfirmationResponse response = bookstoreUserRegister.jwtlogin(confirmationPayload);
        String token = response.getResult();
        assertNotNull(token);
        //извлекаемое из токена имя совпадает с email полезной нагрузки
        assertTrue(CoreMatchers.is(jwtUtil.extractUsername(token)).matches(confirmationPayload.getContact()));
    }
}