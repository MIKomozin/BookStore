package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode,Long> {

    SmsCode findByCode(String code);
}
