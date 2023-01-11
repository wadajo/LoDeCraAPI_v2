package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class WrongVolumeNoException extends ErrorResponseException {

    public WrongVolumeNoException(String bookId, Integer volumeNum) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail(bookId,volumeNum),null);
    }

    private static ProblemDetail asProblemDetail(String bookId, Integer volumeNum) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "No se encontró el ejemplar nro. "+volumeNum+" del libro con código "+bookId);
        problemDetail.setTitle("Ejemplar inexistente");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
