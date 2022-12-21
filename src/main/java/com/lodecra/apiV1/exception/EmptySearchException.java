package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class EmptySearchException extends ErrorResponseException {

    public EmptySearchException() {
        super(HttpStatus.NOT_FOUND, asProblemDetail(), null);
    }

    private static ProblemDetail asProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "La búsqueda no produjo resultados");
        problemDetail.setTitle("Sin resultados");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
