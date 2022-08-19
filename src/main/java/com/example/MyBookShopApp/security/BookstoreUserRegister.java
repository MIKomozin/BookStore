package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.entity.TokenBlackList;
import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

//сервис для регистрации и логирования пользователя
@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final TokenBlackListService tokenBlackListService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;//проверка подлинности учетных данных клиента
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, TokenBlackListService tokenBlackListService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.tokenBlackListService = tokenBlackListService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void registerNewUser(RegistrationForm registrationForm) throws UserExistException {
    //добавление нового пользователя в БД, но сначала проверяем есть ли пользователь с таким email в БД
        BookstoreUser bookstoreUserByEmail = bookstoreUserRepository.findUserByEmail(registrationForm.getEmail());
        BookstoreUser bookstoreUserByPnone = bookstoreUserRepository.findUserByPhone(registrationForm.getPhone());
        if (bookstoreUserByEmail != null && bookstoreUserByPnone != null) {
            throw new UserExistException("пользователь с таким email и телефоном существует. Введите другие данные");
        } else if (bookstoreUserByEmail != null) {
            throw new UserExistException("пользователь с таким email существует. Введите другой email");
        } else if (bookstoreUserByPnone != null) {
            throw new UserExistException("пользователь с таким телефоном существует. Введите другой телефон.");
        } else {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));//шифруем пароль, в БД будут строка непонятный символов
            bookstoreUserRepository.save(user);
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

        //после того как прошли аутентификацию нужно добавить токен в blackList
//        Integer userId = tokenBlackListService.getUserByEmail(payload.getContact()).getId();
//        String hashToken = tokenBlackListService.getHashFromToken(jwtToken);//генерируем hash для нашего токена, так как токен в чистом виде хранить в БД не безопасно
//        TokenBlackList tokenBlackList = new TokenBlackList();
//        tokenBlackList.setUserId(userId);
//        tokenBlackList.setHash(hashToken);
//        tokenBlackListService.save(tokenBlackList); //добавляем наш токен в blackList

        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;//возвращаем его строковое представление
        //обрати внимание, что здесь мы не устанавливаем объект Authentication в текущий SecurityContext, используемый фреймворком для хранения текущего вошедшего в систему пользователя
        //SecurityContextHolder.getContext().getAuthentication() == null по факту добавим это в фильтре jwtToken
    }

    public Object getCurrentUser() {
        BookstoreUserDetails bookstoreUserDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookstoreUserDetails.getBookstoreUser();
    }

    //при выходе из системы добавляем токе в блэклист
    public void addTokenBlackList(HttpServletRequest request) {
        String token = null;
        String username = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    username = jwtUtil.extractUsername(token);//извлекаем username из созданного jwttokena
                }
            }
        }

        Integer userId = tokenBlackListService.getUserByEmail(username).getId();
        String hashToken = tokenBlackListService.getHashFromToken(token);//генерируем hash для нашего токена, так как токен в чистом виде хранить в БД не безопасно
        TokenBlackList tokenBlackList = new TokenBlackList();
        tokenBlackList.setUserId(userId);
        tokenBlackList.setHash(hashToken);
        tokenBlackListService.save(tokenBlackList);//добавляем наш токен в blackList
    }
}

/*

 */