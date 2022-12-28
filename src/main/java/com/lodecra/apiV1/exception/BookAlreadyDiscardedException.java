package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class BookAlreadyDiscardedException extends ErrorResponseException {

    public BookAlreadyDiscardedException(String title) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail(title), null);
    }

    private static ProblemDetail asProblemDetail(String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El libro con título "+title+" ya está descartado.");
        problemDetail.setTitle("Error al descartar");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
