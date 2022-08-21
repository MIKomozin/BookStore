package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.data.entity.TokenBlackList;
import com.example.MyBookShopApp.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final TokenBlackListService tokenBlackListService;

    @Autowired
    public JWTRequestFilter(BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, TokenBlackListService tokenBlackListService) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = request.getCookies();
        boolean isBlackList = false;

        //ранее мы вошли в букшоп как пользователь магазина нам присвоился cookie файл с именем "token"
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    username = jwtUtil.extractUsername(token);//извлекаем username из созданного jwttokena
                }

                //проверяем находится ли token в blackList, если да то не будем аутенфицировать пользователся
                if (username != null) {
                    Integer userId = tokenBlackListService.getUserByEmail(username).getId();
                    String tokenHash = tokenBlackListService.getHashFromToken(token);
                    TokenBlackList findTokenInBlackList = tokenBlackListService.getTokenBlackListByUserIdAndHash(userId, tokenHash);
                    if (findTokenInBlackList != null) {
                        isBlackList = true;
                    }
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && !isBlackList) {
                    BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);//Spring Security хранит основную информацию о каждом аутентифицированном пользователе, поэтому воспользуемся фреймворком для хранения текущего вошедшего в систему пользователя
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}