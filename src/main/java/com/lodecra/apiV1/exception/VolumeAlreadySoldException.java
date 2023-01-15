package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class VolumeAlreadySoldException extends ErrorResponseException {

    public VolumeAlreadySoldException(String bookId, Integer volumeNum) {
        super(HttpStatus.BAD_REQUEST, asProblemDetail(bookId,volumeNum),null);
    }

    private static ProblemDetail asProblemDetail(String bookId, Integer volumeNum) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "El ejemplar nro. "+volumeNum+" del libro con código "+bookId+" ya fue vendido con anterioridad.");
        problemDetail.setTitle("Ejemplar ya vendido");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
