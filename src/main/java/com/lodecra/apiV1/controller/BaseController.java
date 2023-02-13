package com.lodecra.apiV1.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "LoDeCraLogger")
public class BaseController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ProblemDetail> handleCodeValidationExceptions(ConstraintViolationException ex) {
        var listaDeProblemas=new ArrayList<ProblemDetail>();
        ex.getConstraintViolations().stream().toList().forEach((error) -> {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error.");
            problemDetail.setTitle("Validation error");
            problemDetail.setDetail(error.getMessage());
            log.error(error.getMessage()+": "+error.getInvalidValue());
            listaDeProblemas.add(problemDetail);
        });
        return listaDeProblemas;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var listaDeProblemas=new ArrayList<ProblemDetail>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error.");
            String fieldName = ((FieldError) error).getField();
            problemDetail.setTitle("Error in field "+fieldName+": "+error.getDefaultMessage());
            log.error("Error in field "+fieldName+": "+error.getDefaultMessage());
            listaDeProblemas.add(problemDetail);
        });
        return listaDeProblemas;
    }

}

