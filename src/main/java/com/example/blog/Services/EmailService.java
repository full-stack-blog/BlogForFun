package com.example.blog.Services;

import com.example.blog.Models.Post;
import com.example.blog.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("blog4fun123@gmail.com")
    private String from;

    public void prepareAndSend2(User user, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(user.getEmail());
        msg.setTo(from);
        msg.setSubject(subject);
        msg.setText(body);

        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

    public void prepareAndSend(Post post, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(post.getUser().getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
//    public void prepareAndSend2(User user, String subject, String body) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
//        msg.setTo(user.getEmail());
//        msg.setSubject(subject);
//        msg.setText(body);
//
//        try {
//            this.emailSender.send(msg);
//        } catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }

//    public EmailService(JavaMailSender emailSender, String subject, String body) {
//        this.emailSender = emailSender;
//        this.from = from;
//        this.subject = subject;
//    }
//
//    public JavaMailSender getEmailSender() {
//        return emailSender;
//    }
//
//    public void setEmailSender(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }
//
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }

}