package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    Optional<List<Libro>> obtenerTodosLosLibrosDisponibles();

    List<Libro> obtenerLibrosDisponiblesPorBusquedaGeneral(String keyword);

    List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar);

    Optional<Libro> obtenerLibroPorCodigo(String codigo);

    Optional<Libro> crearNuevoLibro(Libro nuevo) throws BookNotSavedException;

    Optional<Libro> buscarLibroPorTituloYAutor(String titulo, String autor);

    Optional<Libro> editarLibroExistente(Libro editadoSinCodigo, String codLibro);

    void descartarLibro(String codLibro);
}
