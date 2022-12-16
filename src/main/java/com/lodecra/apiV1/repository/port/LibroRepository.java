package com.lodecra.apiV1.repository.port;

import com.lodecra.apiV1.model.Libro;

import java.util.List;

public interface LibroRepository {

    List<Libro> obtenerTodosLosLibros();
    List<Libro> obtenerLibrosPorBusquedaGeneral(String keyword);

    List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar);
}
