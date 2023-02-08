package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.model.Venta;

import java.util.List;

public interface VentaService {

    Venta hacerVentaRapida (String codLibro, Integer nroEjemplar) throws VolumeAlreadySoldException,WrongIdFormatException;

    List<Venta> listarVentasDelLibro (String codLibro) throws WrongIdFormatException;

}
