package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Integer> {

    TokenBlackList findTokenBlackListByUserIdAndHash(Integer userId, String hash);
}
