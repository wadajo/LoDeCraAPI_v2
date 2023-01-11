package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.model.Ejemplar;

import java.util.List;
import java.util.Optional;

public interface EjemplarService {

    Optional<List<Ejemplar>> getEjemplaresPorCodigoLibro (String codLibro) throws WrongIdFormatException, BookNotFoundException;

    Optional<Ejemplar> getEjemplarNro (String codLibro, Integer nroEjemplar) throws WrongVolumeNoException;

    Ejemplar guardarNuevoEjemplar(String codLibro, String ubicacion, String modalidad) throws BookNotSavedException;

}
