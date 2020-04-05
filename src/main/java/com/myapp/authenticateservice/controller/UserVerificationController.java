package com.myapp.authenticateservice.controller;

import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.myapp.authenticateservice.model.User;
import com.myapp.authenticateservice.repository.UserRepository;
import com.myapp.authenticateservice.service.CognitoUserPoolService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management")
public class UserVerificationController {

    private CognitoUserPoolService cognitoService;

    @Autowired
    public UserVerificationController(CognitoUserPoolService cognitoUserPoolService){
        this.cognitoService=cognitoUserPoolService;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getCognitoUser(@PathVariable String userId){
        User user= cognitoService.getUser(userId);
        return new ResponseEntity("User "+user.getUserName()+" exists in cognito pool",HttpStatus.OK);

    }


    @PostMapping("/users/verify")
    public ResponseEntity verifyUser( ){
        User user=cognitoService.addUser();
        return new ResponseEntity("Added user "+user.getUserName()+ " to the cognito user pool",HttpStatus.CREATED);
    }

    @ExceptionHandler(value= {UserNotFoundException.class})
    public ResponseEntity userNotfoundExceptionHandler(UserNotFoundException e){
        return new ResponseEntity(e.getErrorMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= UsernameExistsException.class)
    public ResponseEntity userExistsExceptionHandler(UsernameExistsException e){
        return new ResponseEntity(e.getErrorMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= NullPointerException.class)
    public ResponseEntity resourceNotFoundException(NullPointerException e){
        return new ResponseEntity("Please Check if User Exists in Database:",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AWSCognitoIdentityProviderException.class)
    public ResponseEntity cognitoExceptionHandler(AWSCognitoIdentityProviderException e){
        return new ResponseEntity(e.getErrorMessage(),HttpStatus.BAD_REQUEST);

    }

}
