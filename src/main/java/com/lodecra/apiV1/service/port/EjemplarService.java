package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.model.Ejemplar;

import java.util.List;
import java.util.Optional;

public interface EjemplarService {

    List<Ejemplar> getEjemplaresDisponiblesPorCodigo(String codLibro) throws BookNotFoundException;

    Optional<Ejemplar> getEjemplarNro (String codLibro, Integer nroEjemplar);

    Ejemplar guardarNuevoEjemplar(String codLibro, String ubicacion, String modalidad) throws BookNotSavedException;

}
