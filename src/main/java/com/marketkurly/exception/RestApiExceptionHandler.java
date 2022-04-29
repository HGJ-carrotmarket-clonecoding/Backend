package com.marketkurly.exception;


import com.marketkurly.dto.responseDto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = {
            DuplicateUserException.class,
            EmailFormException.class,
            EmptyException.class,
            JwtTokenExpiredException.class,
            LengthException.class,
            NoItemException.class,
            TokenNullException.class,
            UnauthenticatedException.class,
            NoneLoginException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<Object> RestApiExceptionHandle(RuntimeException ex) {
        ResponseDto restApiException = new ResponseDto("failed", ex.getMessage(), "");
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.BAD_REQUEST
        );
    }
}