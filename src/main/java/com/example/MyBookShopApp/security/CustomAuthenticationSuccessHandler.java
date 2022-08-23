package com.example.MyBookShopApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final BookStoreUserFromOtherService bookStoreUserFromOtherService;

    @Autowired
    public CustomAuthenticationSuccessHandler(BookStoreUserFromOtherService bookStoreUserFromOtherService) {
        this.bookStoreUserFromOtherService = bookStoreUserFromOtherService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        bookStoreUserFromOtherService.registerNewUser(user);
        //BookstoreUser bookstoreUser = bookStoreUserFromOtherService.findUserByName(user.getName());//берем данного пользователя из БД


        response.sendRedirect("/my");
    }

}
