package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class NoExistencesFoundException extends ErrorResponseException {

    public NoExistencesFoundException(String bookId) {
        super(HttpStatus.NOT_FOUND, asProblemDetail(bookId), null);
    }

    private static ProblemDetail asProblemDetail(String bookId) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "No se encontraron ejemplares disponibles o no vendidos del libro con código "+bookId);
        problemDetail.setTitle("No hay ejemplares disponibles");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
