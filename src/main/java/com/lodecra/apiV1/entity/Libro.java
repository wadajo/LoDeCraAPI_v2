package com.lodecra.apiV1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    private String id, titulo, autor;
    private Integer precio;
    private String editorial, contacto;
    private Integer stock;
    private Boolean descartado;

}
