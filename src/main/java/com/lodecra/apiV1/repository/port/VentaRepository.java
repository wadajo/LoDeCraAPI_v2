package com.lodecra.apiV1.repository.port;

public interface VentaRepository {

    boolean estaVendido(String codLibro, Integer nroEjemplar);
}
