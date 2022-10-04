package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PaymentService {

    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String firstTestPass;


    public String getPaymentUrl(List<Book> booksFromCookieSlugs) throws NoSuchAlgorithmException {
        Integer paymentSumTotal = booksFromCookieSlugs.stream().mapToInt(Book::discountPrice).sum();
        MessageDigest md = MessageDigest.getInstance("MD5");//класс для хэширования
        String invId = "5"; //индекс заказа, задаем сами (тестовый режим), MD5 - метод шифрования
        md.update((merchantLogin + ":" + paymentSumTotal.toString() + ":" + invId + ":" + firstTestPass).getBytes());//создаем хэш (массив байтов) для передачи в робокассу для дальнейше оплаты
        return "https://auth.robokassa.ru/Merchant/Index.aspx"+
                "?MerchantLogin=" + merchantLogin +
                "&IndId=" + invId +
                "&Culture=" + "ru" +
                "&Encoding=" + "utf-8" +
                "&OutSum=" + paymentSumTotal.toString() +
                "&SignatureValue="+ DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=" + "1";
    }
}
