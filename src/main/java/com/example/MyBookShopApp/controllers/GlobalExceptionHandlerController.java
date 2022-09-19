package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.AOP.annotations.HandleExceptionRestApi;
import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.errs.BookStoreApiWrongParameterException;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice//методы данного компонента будут использоваться несколькими контроллерами
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UserNotFound", e);
        return "redirect:/signin";
    }

    @ExceptionHandler(UserExistException.class)
    public String handleUserExistException(UserExistException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UserExist", e);
        return "redirect:/signup";
    }

    //обработчики исключений для внешнего REST API
    @HandleExceptionRestApi
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", exception), HttpStatus.BAD_REQUEST);
    }

    @HandleExceptionRestApi
    @ExceptionHandler(BookStoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookStoreApiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Bad parameter value", exception), HttpStatus.BAD_REQUEST);
    }
}
