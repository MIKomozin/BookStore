package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.PaymentDTO;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.data.dto.DataProfile;
import com.example.MyBookShopApp.security.data.entity.BookstoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class ProfileController {

    private final BookstoreUserRegister bookstoreUserRegister;
    private final PaymentService paymentService;

    @Autowired
    public ProfileController(BookstoreUserRegister bookstoreUserRegister, PaymentService paymentService) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.paymentService = paymentService;
    }

    //переход на страницу профиля зарегестрированного пользвателя
    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", bookstoreUserRegister.getCurrentUser());
        return "profile";
    }

    @PostMapping(value = "/profile/saveDataUsers", consumes = "application/x-www-form-urlencoded")
    public String handleChangeUsersData(DataProfile payload, Model model) {
        ContactConfirmationResponse response = bookstoreUserRegister.changeDataUser(payload);
        model.addAttribute("profileMessage", response.getResult());
        model.addAttribute("currentUser", bookstoreUserRegister.getCurrentUser());
        return "profile";
    }

    @PostMapping("/payment")
    public RedirectView handlePayment(@RequestBody PaymentDTO paymentDTO) throws NoSuchAlgorithmException {
        String paymentUrl = paymentService.getPaymentUrl(paymentDTO.getSum());
        return new RedirectView(paymentUrl);
    }

//    @GetMapping("/payment")
//    public String handlePaymentAnswer(@RequestParam Map<String, String> params) {
//        BookstoreUser user = bookstoreUserRegister.getCurrentUser();
//        user.setBalance(user.getBalance() + params.get("OutSum"));
//        return "profile";
//    }
}
// http://www.mybookshoptest/ OutSum