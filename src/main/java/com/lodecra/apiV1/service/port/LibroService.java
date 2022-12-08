package com.lodecra.apiV1.service.port;

import com.lodecra.apiV1.model.Libro;

import java.util.List;

public interface LibroService {

    List<Libro> getLibros();
    List<Libro> getLibrosPorBusquedaGral(String keyword);
    List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar);
    Libro getLibroPorId(String id);
    Boolean existeLibroConMismoCodigo(String id);
}
