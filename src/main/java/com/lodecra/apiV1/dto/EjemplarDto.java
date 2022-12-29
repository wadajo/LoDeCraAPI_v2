package com.lodecra.apiV1.dto;

import java.time.LocalDateTime;

public record EjemplarDto(
        LibroDto book,
        Integer volumeNum,
        String location,
        String modality,
        LocalDateTime added
) {}
