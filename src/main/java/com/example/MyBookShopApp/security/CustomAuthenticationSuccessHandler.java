package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.errs.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final BookStoreUserFromOtherService bookStoreUserFromOtherService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public CustomAuthenticationSuccessHandler(BookStoreUserFromOtherService bookStoreUserFromOtherService, HandlerExceptionResolver handlerExceptionResolver) {
        this.bookStoreUserFromOtherService = bookStoreUserFromOtherService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        Logger.getLogger(this.getClass().getSimpleName()).info("user: " + user.toString());
        BookstoreUser bookstoreUser = bookStoreUserFromOtherService.convertNewUserToBookstoreUser(user);

        //добавляем нового пользователя в БД, если такого не сущестувет
        try {
            bookStoreUserFromOtherService.registerNewUser(bookstoreUser);
        } catch (UserExistException userExistException) {
            handlerExceptionResolver.resolveException(request, response, null, userExistException);
        }

        //переаутентифицируем вошедшего пользователя
//        bookStoreUserFromOtherService.authenticationNewUser(bookstoreUser);

        response.sendRedirect("/my");
    }

}
