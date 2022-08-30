package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

//сервис для регистрации и логирования пользователя
@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;//проверка подлинности учетных данных клиента
    private final BookStoreUserFromOtherService bookStoreUserFromOtherService;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookStoreUserFromOtherService bookStoreUserFromOtherService) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserFromOtherService = bookStoreUserFromOtherService;
    }

    public BookstoreUser registerNewUser(RegistrationForm registrationForm) {
    //добавление нового пользователя в БД, но сначала проверяем есть ли пользователь с таким email в БД
        BookstoreUser bookstoreUserByEmail = bookstoreUserRepository.findUserByEmail(registrationForm.getEmail());
        BookstoreUser bookstoreUserByPnone = bookstoreUserRepository.findUserByPhone(registrationForm.getPhone());
        if (bookstoreUserByEmail == null && bookstoreUserByPnone == null) {
//            throw new UserExistException("пользователь с таким email и телефоном существует. Введите другие данные");
//        } else if (bookstoreUserByEmail != null) {
//            throw new UserExistException("пользователь с таким email существует. Введите другой email");
//        } else if (bookstoreUserByPnone != null) {
//            throw new UserExistException("пользователь с таким телефоном существует. Введите другой телефон.");
//        } else {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));//шифруем пароль, в БД будут строка непонятных символов
            bookstoreUserRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

//    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
//                payload.getCode())); //аутентификация переданного объекта по email и паролю, возвращает заполненный Authentication объект (включая предоставленные полномочия) в случае успеха
//        SecurityContextHolder.getContext().setAuthentication(authentication);//установим полученный объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
//        ContactConfirmationResponse response = new ContactConfirmationResponse();
//        response.setResult("true");
//        return response;
//    }

    public ContactConfirmationResponse jwtlogin(ContactConfirmationPayload payload) throws UsernameNotFoundException {
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());//находим пользвателя из БД по email(из полезной нагрузки)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));//аутентификация по логин/паролю
        String jwtToken = jwtUtil.generateToken(userDetails);//генерируем jwtToken для зарегистрированного пользователя, который зашел
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;//возвращаем его строковое представление
        //обрати внимание, что здесь мы не устанавливаем объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
        //SecurityContextHolder.getContext().getAuthentication() == null по факту добавим это в фильтре jwtToken
    }

    public Object getCurrentUser() {
        BookstoreUserDetails bookstoreUserDetails = null;
        BookstoreUser bookstoreUser = null;
        try {
            bookstoreUserDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            bookstoreUser = bookstoreUserDetails.getBookstoreUser();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getSimpleName()).info("не удалось преобразовать, значит объект класса DefaultOAuth2User");
            DefaultOAuth2User user = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            bookstoreUser = bookStoreUserFromOtherService.convertNewUserToBookstoreUser(user);
            return bookstoreUser;
        }

        return bookstoreUser;
    }

    //проверка аутентификации пользователя (анонимный или зарегистрированный)
    public boolean userIsAuthenticate(){
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        Logger.getLogger(this.getClass().getSimpleName()).info("Authenticate name: " + auth);
        if (auth.equals("anonymousUser")) {
            return false;
        } else {
            return true;
        }
    }
}
