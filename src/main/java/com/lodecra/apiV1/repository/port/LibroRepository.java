package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    List<Libro> obtenerTodosLosLibros();
    List<Libro> obtenerLibrosPorBusquedaGeneral(String keyword);
    List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar);

    Optional<Libro> obtenerLibroPorCodigo(String codigo);
}
