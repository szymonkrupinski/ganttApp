package com.example.authorization.fasada;

import com.example.authorization.entity.*;
import com.example.authorization.exceptions.UserEmailExists;
import com.example.authorization.exceptions.UserNameExsist;
import com.example.authorization.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> addNewUser(@Valid @RequestBody UserRegisterDTO user){
        try{userService.register(user);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS)) ;

        }catch (UserNameExsist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A4));
        }catch (UserEmailExists e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A5));
        }

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
  public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response){
        log.info("--TRY Login USER");
        return userService.login(response,user);
    }

    @RequestMapping(path = "/validate",method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> validateToken(HttpServletRequest request, HttpServletResponse response){
        try{
            userService.validateToken(request, response);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        }catch (IllegalArgumentException | ExpiredJwtException e){
            return ResponseEntity.status(401).body(new AuthResponse(Code.A3));
        }
    }

    @RequestMapping(path = "/autologin",method = RequestMethod.GET)
    public ResponseEntity<?> autoLogin(HttpServletResponse response, HttpServletRequest request){
        return userService.loginByToken(request,response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleValidationException(MethodArgumentNotValidException ex){
        return new ValidationMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}
