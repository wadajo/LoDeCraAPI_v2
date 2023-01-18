package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Venta;

import java.util.List;

public interface VentaRepository {

    boolean estaVendido(String codLibro, Integer nroEjemplar);

    List<Venta> todasLasVentasDelLibro(String codLibro);
}
