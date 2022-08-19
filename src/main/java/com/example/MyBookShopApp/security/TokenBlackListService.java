package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.entity.TokenBlackList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;
    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public TokenBlackListService(TokenBlackListRepository tokenBlackListRepository, BookstoreUserRepository bookstoreUserRepository) {
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
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
}
