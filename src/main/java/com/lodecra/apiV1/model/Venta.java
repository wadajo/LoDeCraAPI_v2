package com.lodecra.apiV1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    private Ejemplar ejemplarVendido;
    private LocalDateTime fechaHoraVendido;
    private Integer precioVendido;

}
