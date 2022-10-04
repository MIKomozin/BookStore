package com.example.MyBookShopApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${appEmail.email}")
    private String email;

    @Value("${appEmail.password}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(465);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        //добавим настройки для работы с самим SMPT сервером
        Properties props = mailSender.getJavaMailProperties();//вызовем свойство mailSender и добавим несколько свойств
        props.put("mail.transport.protocol","smtps");//установим метод защиты SMTP для шифорвки соединения (передачи данных). Он предназначен для обеспечения аутентификации партнеров по коммуникациям, а также целостности и конфиденциальности данных
        props.put("mail.smpt.auth","true");//при общении с сервером проводится предварительная аутентификация
        props.put("mail.smtp.starttls.enable","true");//доп защита (разобраться)
        props.put("mail.smtp.ssl.enable","true");//это цифровая подпись сайта. С её помощью подтверждается его подлинность

        props.put("mail.debug","true");//получать дебаг-распечатку в консоль отладки

        return mailSender;
    }
}
