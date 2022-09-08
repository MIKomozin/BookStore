package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class CartControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    public CartControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void checkAddBookIntoCart() throws Exception {
        mockMvc.perform(post("/changeBookStatus/{slug}", "book-ilp-658"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().value("cartContents", "book-ilp-658"))
                .andExpect(redirectedUrl("/books/book-ilp-658"));

        //проверим, что при запросе к корзине и наличии данного куки будет отображаться книга (может можно и по другому)
        mockMvc.perform(get("/cart").cookie(new Cookie("cartContents", "book-ilp-658")))//добавляем куки
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div/main/form/div[1]/div/div[2]/div[1]/div[2]/a").string("King of Kings"));
    }

    @Test
    public void checkDeleteBookFromCart() throws Exception {
        mockMvc.perform(post("/changeBookStatus/cart/remove/{slug}", "book-ilp-658")
                        .cookie(new Cookie("cartContents", "book-ilp-658")))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(cookie().value("cartContents", ""));//ждем пустой куки

        //проверим, что при запросе к корзине и отсутсвтии данного куки корзина пустая
        mockMvc.perform(get("/cart"))//куки cartContent пустой
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div/main/h3").string("Корзина пуста"));
    }

}