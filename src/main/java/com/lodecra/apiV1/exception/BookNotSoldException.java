package com.lodecra.apiV1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.time.Instant;

public class BookNotSoldException extends ErrorResponseException {

    public BookNotSoldException(String title, Integer volumeNo) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, asProblemDetail(title, volumeNo), null);
    }

    private static ProblemDetail asProblemDetail(String title, Integer volumeNo) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo hacer la venta del libro con título "+title+" y ejemplar nro. "+volumeNo);
        problemDetail.setTitle("Error al vender");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
