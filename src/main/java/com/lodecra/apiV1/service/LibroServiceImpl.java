package com.lodecra.apiV1.service;

import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    LibroRepository repository;

    public LibroServiceImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Libro> getLibros() {
        return repository.obtenerTodosLosLibros();
    }

    @Override
    public List<Libro> getLibrosPorBusquedaGral(String keyword) {
        return null;
    }

    @Override
    public List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        return null;
    }

    @Override
    public Libro getLibroPorId(String id) {
        return null;
    }

    @Override
    public Boolean existeLibroConMismoCodigo(String id) {
        return null;
    }
}
