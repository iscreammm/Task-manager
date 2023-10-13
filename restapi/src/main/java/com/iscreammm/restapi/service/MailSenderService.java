package com.iscreammm.restapi.service;

import com.iscreammm.restapi.security.config.MailSenderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final MailSenderConfig mailSenderConfig;
    @Autowired
    public MailSenderService(JavaMailSender javaMailSender, MailSenderConfig mailSenderConfig) {
        this.javaMailSender = javaMailSender;
        this.mailSenderConfig = mailSenderConfig;
    }
    public void send(String emailTo, String code) throws MessagingException {
        String subject = "Подтверждение email адреса Meracle";
        String message = "Вы получили данное письмо, так как был подан запрос на подтверждения этого email адреса."
                + " Если вы не подавали этот запрос, просто проигнорируйте данное письмо и примите наши извинения."
                + "<br>"
                + "<br>"
                + "Для подтверждения перейдите по <a href=\"http://localhost:8080/activate/"
                + code
                + "\"> ссылке </a>.";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(message, true);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setFrom(mailSenderConfig.getUsername());

        javaMailSender.send(mimeMessage);
    }
}
