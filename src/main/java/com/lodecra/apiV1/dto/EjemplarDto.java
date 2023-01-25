package com.lodecra.apiV1.dto;

public record EjemplarDto(
        BookDto book,
        Integer volumeNum,
        String location,
        String modality,
        String added
) {}
