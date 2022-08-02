package com.example.MyBookShopApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister bookstoreUserRegister;

    @Autowired
    public AuthUserController(BookstoreUserRegister bookstoreUserRegister) {
        this.bookstoreUserRegister = bookstoreUserRegister;
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

    //добавление в полезную нагрузку телефона или почты
    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    //сравнение отправленного кода на почту или телефон с введенным, пока звглушка по сути
    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    //регистрация нового пользователя после нажатия кнопки зарегистрироватсья
    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        bookstoreUserRegister.registerNewUser(registrationForm);
        model.addAttribute("regOK", true);//добавляем в модель true если пользователь зарегистрирован
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
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


//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request){
//        HttpSession session = request.getSession();//извлекаем текущий сеанс сессии
//        SecurityContextHolder.clearContext();//удаляем значение контекста из текущего потока
//        if (session != null) {
//            session.invalidate(); //аннулируем данную сессию
//        }
//
//        for (Cookie cookie: request.getCookies()) {
//            cookie.setMaxAge(0);//обнуляем все куки ибо пользователь вышел
//        }
//
//        return "redirect:/";
//    }
}
