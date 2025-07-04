package com.mana.library.exeptionhandler;

import com.mana.library.exeptionhandler.exeption.EmailAlreadyUsedException;
import com.mana.library.exeptionhandler.exeption.PenalityFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EmailAlreadyUsedException.class})
    public ResponseEntity<ApiError> emailAlreadyUsed(EmailAlreadyUsedException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({MailException.class})
    public ResponseEntity<ApiError> mailException(MailException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to send email: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(PenalityFoundException.class)
    public ResponseEntity<ApiError> handlePenalityPresentException(PenalityFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
