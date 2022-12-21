package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class WrongIdFormatException extends ErrorResponseException {

    public WrongIdFormatException(String bookId) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail(bookId),null);
    }

    private static ProblemDetail asProblemDetail(String bookId) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El código "+bookId+" tiene un formato incorrecto");
        problemDetail.setTitle("Formato incorrecto de código");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
