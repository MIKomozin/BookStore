package com.example.MyBookShopApp.security.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AuthUserControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    public AuthUserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void accessOnlyAuthorizedPageFailTest() throws Exception {
        mockMvc.perform(get("/my"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    @WithUserDetails("m.i.komozin@gmail.com")
    public void testAuthenticatedAccessToProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/header/div[1]/div/div/div[3]/div/a[4]/span[1]").string("Дмитрий Петров"));

    }

    @Test
    public void correctLoginTestByEmail() throws Exception {
        mockMvc.perform(formLogin("/signin").user("m.i.komozin@gmail.com").password("12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void correctLoginTestByPhone() throws Exception {
        mockMvc.perform(formLogin("/signin").user("+7 (222) 222-22-22").password("12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void correctRegistration() throws Exception {
        mockMvc.perform(post("/reg")
                        .param("name", "Daddy")
                        .param("phone", "+7 (906) 932-15-50")
                        .param("email", "daddy@mail.com")
                        .param("password", "12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

    @Test
    public void checkLogout() throws Exception {
        mockMvc.perform(logout().logoutUrl("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().maxAge("token", 0))
                .andExpect(cookie().value("token", (String) null))
                .andExpect(redirectedUrl("/signin"));
    }

}