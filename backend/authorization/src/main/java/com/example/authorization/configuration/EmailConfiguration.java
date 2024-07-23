package com.example.authorization.configuration;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    private String email;
    private String password;
    private Authenticator authenticator;
    private Properties properties;
    private Session session;


    public EmailConfiguration(@Value("${notification.mail}") String email, @Value("${notification.password}")String password) {
        this.email = email;
        this.password = password;
    }

    private void config(){
        String SMTP_HOST = "smtp.gmail.com";
        int SMTP_PORT = 587;

        properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties .put("mail.smtp.port", String.valueOf(SMTP_PORT));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", SMTP_HOST);

        this.authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return  new PasswordAuthentication(email, password);
            }
        };
    }

    private void refreshSession(){
        session = Session.getDefaultInstance(properties, authenticator);
    }

    public void sendEmail(String to, String subject, String body, boolean onCreate){
        if (session == null){
            refreshSession();
        }try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse((to)));
            message.setSubject(subject);
            MimeBodyPart mimeBodyPart =new MimeBodyPart();
            mimeBodyPart.setContent(body,"text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);

        }catch (MessagingException e){
            e.printStackTrace();
            if(onCreate){
                refreshSession();
                sendEmail(to,subject,body,false);
            }
        }

    }
}
