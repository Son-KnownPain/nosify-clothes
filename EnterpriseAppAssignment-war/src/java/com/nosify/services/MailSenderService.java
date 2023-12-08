package com.nosify.services;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class MailSenderService {
    private JavaMailSender mailSender;

    public JavaMailSender getMailSender() {
        return mailSender;
    }
    
    public void sendVerifyLinkEmailMail(String to, String verifyUrl) {
        try {
            String from = "nhsona21171@cusc.ctu.edu.vn";
            String senderName = "NOSIFY CLOTHES";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            
            messageHelper.setSubject("NOSIFY CLOTHES - Verify Your Emai");
            messageHelper.setTo(to);
            messageHelper.setFrom(from, senderName);
            
            String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "NOSIFY.";
            
            content = content.replace("[[name]]", to);
            String siteUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
            content = content.replace("[[URL]]", siteUrl + verifyUrl);
            
            messageHelper.setText(content, true);
            
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            Logger.getLogger(MailSenderService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMimeEmailForgotPasswordCode(String to, String forgotPasswordCode, String name) {
        try {
            String from = "nhsona21171@cusc.ctu.edu.vn";
            String senderName = "NOSIFY CLOTHES";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            
            messageHelper.setSubject("Forgot Password Code Number");
            messageHelper.setTo(to);
            try {
                messageHelper.setFrom(from, senderName);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MailSenderService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String content = "Dear [[name]],<br>"
                    + "This is forgot password code to confirm:<br>"
                    + "<h3 style=\"color: blue; font-size: 30px;\">[[code]]</h3>"
                    + "Thank you,<br>"
                    + "NOSIFY.";
            content = content.replace("[[name]]", name);
            content = content.replace("[[code]]", forgotPasswordCode);
            
            messageHelper.setText(content, true);
            
            mailSender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(MailSenderService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
