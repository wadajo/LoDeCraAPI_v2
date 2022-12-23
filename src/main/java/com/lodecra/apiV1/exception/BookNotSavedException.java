package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class BookNotSavedException extends ErrorResponseException {

    public BookNotSavedException(String title) {
        super(HttpStatus.NO_CONTENT, asProblemDetail(title), null);
    }

    private static ProblemDetail asProblemDetail(String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NO_CONTENT, "No se guardó el libro con título "+title);
        problemDetail.setTitle("Error al guardar");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
