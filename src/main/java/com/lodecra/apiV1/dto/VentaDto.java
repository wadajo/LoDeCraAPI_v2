package com.lodecra.apiV1.dto;

public record VentaDto(
        EjemplarDto volume,
        Integer priceSold,
        String dateSold
) {}
