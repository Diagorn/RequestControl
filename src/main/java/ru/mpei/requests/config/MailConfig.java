package ru.mpei.requests.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String mailDebug;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setPort(port);
        sender.setHost(host);

        Properties properties = sender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", mailDebug);

        return sender;
    }
}
