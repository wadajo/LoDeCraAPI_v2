package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Ejemplar;

import java.util.List;
import java.util.Optional;

public interface EjemplarRepository {

    Optional<List<Ejemplar>> obtenerEjemplaresPorCodigoDeLibro(String codLibro);

    Optional<Ejemplar> obtenerEjemplarNro(String codLibro, Integer numEjemplar);

    Ejemplar agregarEjemplar(Ejemplar nuevo);

}
