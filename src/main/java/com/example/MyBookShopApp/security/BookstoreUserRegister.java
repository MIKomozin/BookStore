package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//сервис для регистрации и логирования пользователя
@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;//проверка подлинности учетных данных клиента
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void registerNewUser(RegistrationForm registrationForm) {
    //добавление нового пользователя в БД, но сначала проверяем есть ли пользователь с таким email в БД
        if(bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null) {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));//шифруем пароль, в БД будут строка непонятный символов
            bookstoreUserRepository.save(user);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode())); //аутентификация переданного объекта по email и паролю, возвращает заполненный Authentication объект (включая предоставленные полномочия) в случае успеха
        SecurityContextHolder.getContext().setAuthentication(authentication);//установим полученный объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtlogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());//находим пользвателя из БД по email(из полезной нагрузки)
        String jwtToken = jwtUtil.generateToken(userDetails);//генерируем jwtToken для зарегистрированного пользователя, который зашел
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;//возвращаем его строковое представление
        //обрати внимани, что здесь мы не устанавливаем объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
        //SecurityContextHolder.getContext().getAuthentication() == null по факту это исправим в фильтре
    }

    public Object getCurrentUser() {
        BookstoreUserDetails bookstoreUserDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookstoreUserDetails.getBookstoreUser();
    }
}
