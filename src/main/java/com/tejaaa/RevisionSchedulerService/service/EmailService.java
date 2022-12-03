package com.tejaaa.RevisionSchedulerService.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.protocol}")
    private String protocol;



    public boolean sendPlainTextEmail(String toAdress, String subject,String body){

        JavaMailSenderImpl  mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("gateknowledgecoa@gmail.com");
        msg.setTo(toAdress);
        msg.setSubject(subject);
        msg.setText(body);
        log.info("Sending simple text email {} ",msg);
        mailSender.send(msg);
        return true;
    }

    public JavaMailSenderImpl getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("spring.mail.properties.mail.transport.protocol", "smtp");
        properties.setProperty("spring.mail.properties.mail.smtp.timeout", "5000");
        properties.setProperty("spring.mail.properties.mail.smtp.connectiontimeout", "5000");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("spring.mail.properties.mail.smtp.starttls.required", "true");
        properties.setProperty("spring.mail.properties.mail.smtp.starttls.enable", "true");


//		properties.setProperty("mail.smtp.starttls.enable",smtpTls);
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
