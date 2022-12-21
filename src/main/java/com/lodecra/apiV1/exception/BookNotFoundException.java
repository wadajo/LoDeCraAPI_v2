package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class BookNotFoundException extends ErrorResponseException {

    public BookNotFoundException(String bookId) {
        super(HttpStatus.NOT_FOUND, asProblemDetail(bookId), null);
    }

    private static ProblemDetail asProblemDetail(String bookId) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "No se encontró el libro con código "+bookId);
        problemDetail.setTitle("No encontrado");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
