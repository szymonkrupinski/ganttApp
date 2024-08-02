package com.example.authorization.fasada;

import com.example.authorization.entity.*;
import com.example.authorization.exceptions.UserDontExists;
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
@CrossOrigin
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
        log.info("--TRY Auto Login USER");
        return userService.loginByToken(request,response);
    }

    @RequestMapping(path = "/logged",method = RequestMethod.GET)
    public ResponseEntity<?> loggedIn(HttpServletResponse response,HttpServletRequest request){
        log.info("--TRY Auto Login USER");
        return userService.logged(request,response);
    }

    @RequestMapping(path = "/activate", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> activateUser(@RequestParam String uid){
        try{
            userService.activateUserAcc(uid);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        }catch (UserDontExists e){
            return ResponseEntity.status(400).body(new AuthResponse(Code.A6));
        }
    }
@RequestMapping(path = "/reset-password",method = RequestMethod.POST)
public ResponseEntity<AuthResponse> sendRecoveryEmail(@RequestBody ResetPasswordData resetPasswordData ){
        try{
            userService.recoveryPassword(resetPasswordData.getEmail());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));

        }catch (UserDontExists e){
            return ResponseEntity.status(400).body(new AuthResponse(Code.A6));
        }
}

    @RequestMapping(path = "/reset-password",method = RequestMethod.PATCH)
    public ResponseEntity<AuthResponse> recoveryEmail(@RequestBody ChangePasswordData changePasswordData ){
        try{
            userService.resetPassword(changePasswordData);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));

        }catch (UserDontExists e){
            return ResponseEntity.status(400).body(new AuthResponse(Code.A6));
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleValidationException(MethodArgumentNotValidException ex){
        return new ValidationMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}
