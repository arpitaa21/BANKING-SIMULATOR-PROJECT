package com.infosys.bankingApplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackageClasses = AccountController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail badRequest(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail notFound(NoSuchElementException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(com.infosys.bankingApplication.exceptions.InsufficientFundsException.class)
    public ProblemDetail insufficientFunds(com.infosys.bankingApplication.exceptions.InsufficientFundsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}
