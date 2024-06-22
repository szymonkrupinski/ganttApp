package com.example.authorization.entity;


import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserRegisterDTO {

    @Length(min = 5, max = 30, message = "Login powinien zawierać od 5 do 30 znaków")
    private String login;
    @Email(message = "Email musi być poprawny username@domain.com")
    private String email;
    @Length(min = 8, max = 50, message = "Hasło  powinieno zawierać 8 do 50 znaków")
    private String password;
    private Role role;

}
