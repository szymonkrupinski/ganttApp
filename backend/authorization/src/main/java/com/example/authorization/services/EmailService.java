package com.example.authorization.services;

import com.example.authorization.configuration.EmailConfiguration;
import com.example.authorization.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailConfiguration emailConfiguration;

    @Value("classpath:static/active.html")
    Resource activeAcconutTemplate;

    public void sendActivation(User user) {
        try{

        }
    }
}
