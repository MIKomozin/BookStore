package com.example.MyBookShopApp.security.config;

import com.example.MyBookShopApp.security.data.BookstoreUserDetailsService;
import com.example.MyBookShopApp.security.data.TokenBlackListService;
import com.example.MyBookShopApp.security.data.jwt.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter jwtRequestFilter;
    private final TokenBlackListService tokenBlackListService;

    @Autowired
    public SecurityConfig(BookstoreUserDetailsService bookstoreUserDetailsService,
                          JWTRequestFilter jwtRequestFilter,
                          TokenBlackListService tokenBlackListService) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookstoreUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile").hasRole("USER")//authenticated()
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")
                .and().logout().logoutUrl("/logout").addLogoutHandler(
                        (request, response, authentication) -> {
                            tokenBlackListService.addTokenBlackList(request);
                        })
                .logoutSuccessUrl("/signin").deleteCookies("token");
                //далее проект ведем без функционала Oauth
                //.and().oauth2Login()
                //.and().oauth2Client();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//работаем без sessionId для Cookie
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);//добавляем наш фильтр при работе с jwttokens
    }
}