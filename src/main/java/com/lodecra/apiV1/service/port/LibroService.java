package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    Optional<List<Libro>> getLibros() throws EmptySearchException;

    Optional<List<Libro>> getLibrosPorBusquedaGral(String keyword) throws EmptySearchException;

    Optional<List<Libro>> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) throws EmptySearchException;

    Optional<Libro> getLibroPorCodigo (String codigo) throws WrongIdFormatException, BookNotFoundException;

    Optional<Libro> guardarNuevoLibro (Libro aGuardar) throws BookNotSavedException, DuplicatedBookException;

    boolean existeLibroConMismoTituloYAutor(String titulo, String autor);

    Optional<Libro> editarLibro(Libro editadoSinCodigo, String codigo);

    void descartarLibro(String codigo);
}
