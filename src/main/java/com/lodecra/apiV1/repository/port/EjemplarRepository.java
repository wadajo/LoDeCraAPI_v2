package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;

import java.util.List;
import java.util.Optional;

public interface EjemplarRepository {

    List<Ejemplar> obtenerEjemplaresNoVendidosPorCodigo(String codLibro);

    List<Ejemplar> obtenerEjemplaresTotalesPorCodigo(String codLibro);

    Optional<Ejemplar> obtenerEjemplarNro(String codLibro, Integer numEjemplar);

    void agregarEjemplar(Ejemplar nuevo);

    void venderEjemplar(Venta ventaAHacer);
}
