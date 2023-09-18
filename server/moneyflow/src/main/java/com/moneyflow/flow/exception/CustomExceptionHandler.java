package com.moneyflow.flow.exception;

import com.moneyflow.flow.exception.body.CustomBadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomBadCredentialsException> handleResourceUnauthorizedException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomBadCredentialsException().withCause(ex.getMessage()));
    }

}