package com.lodecra.apiV1.service;

import com.lodecra.apiV1.entity.Libro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LibroService {

    List<Libro> getLibros();
    Libro getLibroPorId(String id);
    List<Libro> getLibrosPorBusquedaGral(String keyword);
    List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar);
}
