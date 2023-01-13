package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Venta;

public interface VentaRepository {

    void saveVenta (Venta aGuardar);

    Venta obtenerVenta(String codLibro, Integer nroEjemplar);
}
