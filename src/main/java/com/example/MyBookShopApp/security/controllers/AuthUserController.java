package com.example.MyBookShopApp.security.controllers;

import com.example.MyBookShopApp.AOP.annotations.HandleException;
import com.example.MyBookShopApp.data.SmsService;
import com.example.MyBookShopApp.data.entity.SmsCode;
import com.example.MyBookShopApp.errs.UserExistException;
import com.example.MyBookShopApp.security.data.BookstoreUserRegister;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.security.data.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.data.dto.DataProfile;
import com.example.MyBookShopApp.security.data.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister bookstoreUserRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AuthUserController(BookstoreUserRegister bookstoreUserRegister, SmsService smsService, JavaMailSender javaMailSender) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
    }

    //переход на страницу входа (войти или зарегистрироваться)
    @GetMapping("/signin")
    public String handleSignIn() {
        return "signin";
    }

    //переход на страницу регистрации (создаем модель форму для ввода данных пользователя)
    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    //при авторизации
    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    //запрос подтверждения контакта при регистрации
    //для телефона (заглушка)
    @PostMapping("/requestPhoneConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestPhoneConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    //для почты
    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("djemboy@mail.ru");//откуда
        message.setTo(payload.getContact());//куда ... информацию о том куда отправляем сообщение, берем из полезной нагрузки. По сути введенный пользвателем email адрес
        SmsCode smsCode = new SmsCode(smsService.generateCode(),300);//5 minutes
        smsService.saveNewCode(smsCode);//сохраняем сгенерированный код в БД
        message.setSubject("Bookstore email verification!");//заголовок письма (чтобы не падало в СПАМ!)
        message.setText("Verification code is: " + smsCode.getCode());//текст письма откуда мы и возьмем наш код
        javaMailSender.send(message);//при помощи javaMailSender отправявлем письмо с кодом нашему пользователю для прохождения авторизации
        response.setResult("true");
        return response;
    }

    //сравнение отправленного кода на почту или телефон с введенным
    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        //такой же пароль вводим и для телефонп (Twilio не работает, найти и подключить другой сервис...)
        if (smsService.verifyCode(payload.getCode())){
            response.setResult("true");
        }
        return response;
    }

    //регистрация нового пользователя после нажатия кнопки зарегистрироватсья
    @HandleException
    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) throws UserExistException {
        bookstoreUserRegister.registerNewUser(registrationForm);
        model.addAttribute("regOK", true);//добавляем в модель true если пользователь зарегистрирован
        return "redirect:/signin";
    }

    @HandleException
    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleUserLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) throws UsernameNotFoundException {
        ContactConfirmationResponse loginResponse = bookstoreUserRegister.jwtlogin(payload);//при аутентификации создаем jwttoken
        Cookie cookie = new Cookie("token", loginResponse.getResult());//создаем куки под именем "token"
        httpServletResponse.addCookie(cookie);//передаем куки в ответе клиенту, теперь у вошедшео пользователя есть cookie по имени "token"
        return loginResponse;
    }

    //переход на страницу моих книг для зарегистрированного пользователя
    @GetMapping("/my")
    public String handleMy() {
        return "/my";
    }

    //переход на страницу профиля зарегестрированного пользвателя
    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", bookstoreUserRegister.getCurrentUser());
        return "/profile";
    }

    @PostMapping("/profile/saveDataUsers")
    public String handleChangeUsersData(@RequestBody DataProfile payload, Model model) {
        ContactConfirmationResponse response = bookstoreUserRegister.changeDataUser(payload);
        model.addAttribute("profileMessage", response.getResult());
        return "redirect:/profile";
    }
}
