package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    List<Libro> getLibros() throws EmptySearchException;
    List<Libro> getLibrosPorBusquedaGral(String keyword) throws EmptySearchException;
    List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) throws EmptySearchException;
    Optional<Libro> getLibroPorCodigo (String codigo) throws WrongIdFormatException, BookNotFoundException;
    Boolean existeLibroConMismoCodigo(String id);
}
