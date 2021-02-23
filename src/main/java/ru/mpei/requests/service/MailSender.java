package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.chats.MessageFile;
import ru.mpei.requests.domain.users.User;

import javax.mail.internet.InternetAddress;
import java.io.File;

@Service
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${upload.path}")
    private String uploadPath;

    public void send(User user, Message message) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(user.getUsername()));
            mimeMessage.setFrom(username);
            mimeMessage.setSubject("Анкета и договор на обучение");
            mimeMessage.setText("Анкета и договор на обучение, сгенерированные в системе контроля заявок");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText("", true);
            for (MessageFile f: message.getMessageFiles()) { //Not working :C
                FileSystemResource file = new FileSystemResource(new File(uploadPath + File.separator + "files" + File.separator + f.getNewFileName()));
                helper.addAttachment(f.getOriginalName(), file);
            }
        };
        mailSender.send(preparator);
    }
}
