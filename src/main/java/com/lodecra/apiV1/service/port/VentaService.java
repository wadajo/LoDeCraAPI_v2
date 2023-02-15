package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.BookNotSoldException;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.model.Venta;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {

    Venta hacerVenta(String codLibro, Integer nroEjemplar, Integer precioVendido, LocalDateTime fechaHoraVendido) throws VolumeAlreadySoldException, BookNotSoldException;

    List<Venta> listarVentasDelLibro (String codLibro);

}
