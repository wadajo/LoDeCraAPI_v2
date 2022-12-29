package com.lodecra.apiV1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ejemplar {

    private Libro libro;
    private Integer nroEjemplar;
    private String ubicacion;
    private String modalidad;
    private LocalDateTime agregado;

}
