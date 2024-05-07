package com.example.authorization.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder

public class UserRegisterDTO {


    private String login;
    private String email;
    private String password;
    private Role role;

}
