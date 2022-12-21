package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    LibroRepository repository;

    public LibroServiceImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Libro> getLibros() {
        var encontrados=repository.obtenerTodosLosLibros();
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    public List<Libro> getLibrosPorBusquedaGral(String keyword) {
        var encontrados=repository.obtenerLibrosPorBusquedaGeneral(keyword);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    public List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        var encontrados=repository.obtenerLibrosPorBusquedaAvz(keyword, campoABuscar);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    public Optional<Libro> getLibroPorCodigo(String codigo) {
        if (codigo.length()!=8) {
            throw new WrongIdFormatException(codigo);
        } else {
            Optional<Libro> encontrado=repository.obtenerLibroPorCodigo(codigo);
            if (encontrado.isPresent())
                return encontrado;
            else
                throw new BookNotFoundException(codigo);
        }
    }

    @Override
    public Boolean existeLibroConMismoCodigo(String id) {
        return null;
    }
}
