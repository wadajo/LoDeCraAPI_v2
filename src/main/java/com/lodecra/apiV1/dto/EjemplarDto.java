package com.lodecra.apiV1.dto;

public record EjemplarDto(
        LibroDto book,
        Integer volumeNum,
        String location,
        String modality,
        String added
) {}
