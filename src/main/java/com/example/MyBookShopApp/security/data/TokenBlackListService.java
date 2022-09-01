package com.example.MyBookShopApp.security.data;

import com.example.MyBookShopApp.data.entity.TokenBlackList;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import com.example.MyBookShopApp.security.data.jwt.JWTUtil;
import com.example.MyBookShopApp.security.data.repository.BookstoreUserRepository;
import com.example.MyBookShopApp.security.data.repository.TokenBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Service
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public TokenBlackListService(TokenBlackListRepository tokenBlackListRepository, BookstoreUserRepository bookstoreUserRepository, JWTUtil jwtUtil) {
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.jwtUtil = jwtUtil;
    }

    public BookstoreUser getUserByEmail(String email) {
        return bookstoreUserRepository.findUserByEmail(email);
    }

    public TokenBlackList getTokenBlackListByUserIdAndHash(Integer userId, String hash) {
        return tokenBlackListRepository.findTokenBlackListByUserIdAndHash(userId, hash);
    }

    public String getHashFromToken(String jwtToken) {
        String hash = jwtToken.substring(0, 5) + "-" + jwtToken.substring(jwtToken.length() - 5);//hash будет иметь такой вид ХХХХХ-ХХХХХ
        return hash;
    }

    public void save(TokenBlackList tokenBlackList) {
        tokenBlackListRepository.save(tokenBlackList);
    }

    //при выходе из системы добавляем токе в блэклист если он есть
    public void addTokenBlackList(HttpServletRequest request) {
        String token = null;
        String username = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    username = jwtUtil.extractUsername(token);//извлекаем username из созданного jwttokena
                    Logger.getLogger(this.getClass().getSimpleName()).info("Added in BlackList next token: " + token);
                }
            }
        }

        //проверяем есть ли куки с ключом "token" и если есть, то добавляем данный токен в блэклист при выходе
        if (token != null) {
            Integer userId = getUserByEmail(username).getId();
            String hashToken = getHashFromToken(token);//генерируем hash для нашего токена, так как токен в чистом виде хранить в БД не безопасно
            TokenBlackList tokenBlackList = new TokenBlackList();
            tokenBlackList.setUserId(userId);
            tokenBlackList.setHash(hashToken);
            save(tokenBlackList);//добавляем наш токен в blackList
        }
    }
}
