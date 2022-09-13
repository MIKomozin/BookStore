package com.example.MyBookShopApp.security.data;

import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.data.dto.RegistrationForm;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.jwt.JWTUtil;
import com.example.MyBookShopApp.security.data.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public BookstoreUser registerNewUser(RegistrationForm registrationForm) throws UserExistException{
        //добавление нового пользователя в БД, но сначала проверяем есть ли пользователь с таким email в БД
        //для тестирования закомментируем все исключения
        BookstoreUser bookstoreUserByEmail = bookstoreUserRepository.findUserByEmail(registrationForm.getEmail());
        BookstoreUser bookstoreUserByPhone = bookstoreUserRepository.findUserByPhone(registrationForm.getPhone());
        if (bookstoreUserByEmail != null && bookstoreUserByPhone != null) {
            //throw new UserExistException("пользователь с таким email и телефоном существует. Введите другие данные");
            return null;
        } else if (bookstoreUserByEmail != null) {
            //throw new UserExistException("пользователь с таким email существует. Введите другой email");
            return null;
        } else if (bookstoreUserByPhone != null) {
            //throw new UserExistException("пользователь с таким телефоном существует. Введите другой телефон.");
            return null;
        } else {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));//шифруем пароль, в БД будут строка непонятных символов
            bookstoreUserRepository.save(user);
            return user;
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) throws UsernameNotFoundException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode())); //аутентификация переданного объекта по email и паролю, возвращает заполненный Authentication объект (включая предоставленные полномочия) в случае успеха
        SecurityContextHolder.getContext().setAuthentication(authentication);//установим полученный объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

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

    public BookstoreUser getCurrentUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof DefaultOAuth2User) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) object;
            String email = defaultOAuth2User.getAttribute("email");
            BookstoreUser authUser = bookstoreUserRepository.findUserByEmail(email);
            if (authUser == null) {
                BookstoreUser bookstoreUser = new BookstoreUser();
                bookstoreUser.setEmail(email);
                bookstoreUser.setName(defaultOAuth2User.getAttribute("name"));
                bookstoreUserRepository.save(bookstoreUser);
                return bookstoreUser;
            } else {
                return authUser;
            }
        } else {
            BookstoreUserDetails bookstoreUserDetails = (BookstoreUserDetails) object;
            return bookstoreUserDetails.getBookstoreUser();
        }
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
