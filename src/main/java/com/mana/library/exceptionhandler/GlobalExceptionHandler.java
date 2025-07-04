package com.mana.library.exceptionhandler;

import com.mana.library.exceptionhandler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

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

    @ExceptionHandler({PenaltyException.class})
    public ResponseEntity<ApiError> handlePenaltyException(PenaltyException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler({AlreadyLoanCopyException.class})
    public ResponseEntity<ApiError> handleAlreadyLoanCopyException(AlreadyLoanCopyException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler({AccountExpiredException.class})
    public ResponseEntity<ApiError> handleAccountExpiredException(AccountExpiredException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
}
