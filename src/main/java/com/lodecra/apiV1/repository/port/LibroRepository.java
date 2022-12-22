package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    Optional<List<Libro>> obtenerTodosLosLibros();
    Optional<List<Libro>> obtenerLibrosPorBusquedaGeneral(String keyword);
    Optional<List<Libro>> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar);

    Optional<Libro> obtenerLibroPorCodigo(String codigo);
}
