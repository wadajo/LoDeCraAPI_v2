package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    List<Libro> getLibrosDisponibles() throws EmptySearchException;

    List<Libro> getLibrosDisponiblesPorBusquedaGral(String keyword) throws EmptySearchException;

    List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) throws EmptySearchException;

    Optional<Libro> getLibroPorCodigo (String codLibro) throws BookNotFoundException;

    Optional<Libro> guardarNuevoLibro (Libro aGuardar) throws BookNotSavedException, DuplicatedBookException;

    boolean existeLibroConMismoTituloYAutor(String titulo, String autor);

    Optional<Libro> editarLibro(Libro editadoSinCodigo, String codLibro);

    void descartarLibro(String codLibro);
}
