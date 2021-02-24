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
        boolean isPhysical = message.getChat().getOrganisationRequest() == null;
        String stringId = isPhysical ? message.getChat().getPhysicalRequest().getId().toString() : message.getChat().getOrganisationRequest().getId().toString();
        String theme = isPhysical ? message.getChat().getPhysicalRequest().getTheme() : message.getChat().getOrganisationRequest().getTheme();
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(user.getUsername()));
            mimeMessage.setFrom(username);
            mimeMessage.setHeader("Content-Type", "text/html; charset=utf-8");
            mimeMessage.setSubject("Анкета и договор на обучение");
            mimeMessage.setText("Анкета и договор на обучение, сгенерированные в системе контроля заявок");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String htmlText = "<html><body>" +
                    "<h3>Здравствуйте, " + user.getPerson().getFirstName() + " " + user.getPerson().getSecondName() +
                    "!</h3>" + "<p>Вы успешно подали заявку на обучение. Пожалуйста проверьте информацию о Вашей заявке ниже.</p>" +
                    "<h5>Информация по вашей заявке: </h5>" +
                    "<p>Номер заявки: " + stringId + "</p>" +
                    "<p>Программа: " + theme + "</p>" +
                    "<br><p>Анкета и заявление размещены в Вашем личном кабинете, а также Вы можете скачать их из этого письма. Их необходимо распечатать и подписать. <u>ВАЖНО!!! Заявление должны подписать в дирекции Вашего института и преподаватель по дисциплине. Подписанные документы необходимо сдать в ауд. Ж-217 Климовой М.А.</u></p>" +
                    "<br>  <p>Если Вы обнаружили неточности и хотите изменить данные по заявке, это можно сделать в Вашем личном кабинете.</p>" +
                    "<br>  <p>Мы рады, что Вы приняли решение пройти обучение у нас!</p>" +
                    "<br>  <p>С уважением,</p><p>Служба поддержки слушателей</p>" +
                    "</body></html>";
            helper.setText(htmlText, true);
            for (MessageFile f: message.getMessageFiles()) { //Not working :C
                FileSystemResource file = new FileSystemResource(new File(uploadPath + File.separator + "files" + File.separator + f.getNewFileName()));
                helper.addAttachment(f.getOriginalName(), file);
            }
        };
        mailSender.send(preparator);
    }
}
