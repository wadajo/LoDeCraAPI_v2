package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.model.Venta;

public interface VentaService {

    void hacerVentaRapida (String codLibro, Integer nroEjemplar);

    Venta obtenerVenta(String codLibro, Integer nroEjemplar);

}
