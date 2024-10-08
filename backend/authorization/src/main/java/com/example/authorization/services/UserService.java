package com.example.authorization.services;


import com.example.authorization.entity.*;
import com.example.authorization.entity.User;
import com.example.authorization.exceptions.UserEmailExists;
import com.example.authorization.exceptions.UserNameExsist;
import com.example.authorization.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service

@RequiredArgsConstructor
    public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CookieService cookieService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    private User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public String generateToken(String username,int exp){
        return jwtService.generateToken(username,exp);
    }

    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException{
        String token = null;
        String refresh = null;
        for(Cookie value : Arrays.stream(request.getCookies()).toList()) {
            if (value.getName().equals("Authorization")) {
                token = value.getValue();
            } else if (value.getName().equals("refresh")) {
                refresh = value.getValue();
            }
        }
        try {
            jwtService.validateToken(token);

        }catch (IllegalArgumentException | ExpiredJwtException e ){
            jwtService.validateToken(refresh);
            Cookie refreshCookie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh,refreshExp),refreshExp);
            Cookie cookie = cookieService.generateCookie("Authorization", jwtService.generateToken(token,exp),exp);
            response.addCookie(cookie);
            response.addCookie(refreshCookie);

        }
    }

    public void register(UserRegisterDTO userRegisterDTO) throws UserNameExsist,UserEmailExists {
        userRepository.findUserByLogin(userRegisterDTO.getLogin()).ifPresent(value->{
            throw new UserNameExsist("Użytkownik o podanej nazwie jest już zarejestrowany w systemie");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value->{
            throw new UserEmailExists("Użytkownik o podanym emailu jest już zarejestrowany w systemie");
        });
        User user = new User();
        user.setLogin(userRegisterDTO.getLogin());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        if(userRegisterDTO.getRole() != null) {
            user.setRole(userRegisterDTO.getRole());
        }else{
            user.setRole(Role.USER);
        }
        saveUser(user);
    }
    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
      User user = userRepository.findUserByLogin(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie("refresh", generateToken(authRequest.getUsername(),refreshExp), refreshExp);
                Cookie cookie = cookieService.generateCookie("token", generateToken(authRequest.getUsername(),exp), exp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build());

            } else {
                return ResponseEntity.ok(new AuthResponse(Code.A1));
            }
        }
        return ResponseEntity.ok(new AuthResponse(Code.A2));
    }





}
