package com.example.authorization.services;

import com.example.authorization.configuration.EmailConfiguration;
import com.example.authorization.entity.User;
import com.example.authorization.repository.UserRepository;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.*;

import java.io.IOException;



@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailConfiguration emailConfiguration;
    @Value("${front.url}")
    private String frontUrl;
    @Value("classpath:static/active.html")
    private Resource activeAcconutTemplate;
    @Value("classpath:static/reset.html")
    private Resource recoveryPassTemplate;

    public void sendActivation(User user) {
        try{
            String html = Files.toString(activeAcconutTemplate.getFile(), Charsets.UTF_8);
            html = html.replace("https://google.com", frontUrl+"/active"+user.getUuid());
            emailConfiguration.sendEmail(user.getEmail(),html,"Aktywacja konta",true);
        }catch (IOException e){
        throw new RuntimeException(e);}
    }

    public void sendPasswordReset(User user, String uid) {
        try{
            log.info("--START sendPasswordRecovery");
            String html = Files.toString(recoveryPassTemplate.getFile(),Charsets.UTF_8);
            html = html.replace("https://google.com", frontUrl+"/odzyskaj-haslo"+uid);
            emailConfiguration.sendEmail(user.getEmail(),html,"Odzyskiwanie hasła",true);
        }catch (IOException e){
            log.info("nie mozna wysłac maila");
        }
    }
}
