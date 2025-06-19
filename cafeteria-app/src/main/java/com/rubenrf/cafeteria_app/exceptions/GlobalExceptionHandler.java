package com.rubenrf.cafeteria_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.rubenrf.cafeteria_app.dto.error.ErrorDTO;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDTO> appExceptionHandler(ApplicationException e) {
        String message = e.getMessage();
        HttpStatus status = e.getStatus();
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }



    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorDTO> httpClientErrorExceptionHandler(HttpClientErrorException.BadRequest e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorDTO> requestNotPermittedExceptionHandler(RequestNotPermitted e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> missingServletRequestParameterExceptionHandler(
            MissingServletRequestParameterException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> methodArgumentTypeMismatchExceptionHandler(
            MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO(message);
        return new ResponseEntity<>(error, status);
    }
}
