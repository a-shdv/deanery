package org.university.deanery.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@Service
@Slf4j
public class EmailSenderService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    public static String fromAddress = "shadaevlab7@gmail.com";
    private final UserService userService;
    public static final String attachment = "/Users/a-shdv/Desktop/report.pdf";

    @Autowired
    public EmailSenderService(JavaMailSender emailSender, UserService userService) {
        this.emailSender = emailSender;
        this.userService = userService;
    }

    public void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }

    public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment) throws MessagingException, FileNotFoundException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(fromAddress);
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);
        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
        messageHelper.addAttachment("/Users/a-shdv/Desktop/report.pdf", file);
        emailSender.send(mimeMessage);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendEmail() throws MessagingException, FileNotFoundException {
//        this.sendEmailWithAttachment(fromAddress, "deanery-app", userService.findAll().toString(), "/Users/a-shdv/IdeaProjects/deanery/test.pdf");
//        log.info("Mail sent successfully");
//    }
}
