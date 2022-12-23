package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class DuplicatedBookException extends ErrorResponseException {

    public DuplicatedBookException(String title, String author) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail(title,author), null);
    }

    private static ProblemDetail asProblemDetail(String title, String author) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "No se guardó el libro con título "+title+" y autor "+author+" porque aparentemente ya existe en la Base.");
        problemDetail.setTitle("Error al guardar: Posible duplicado");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
