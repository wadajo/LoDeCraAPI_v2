package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.LibroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    LibroRepository repository;

    public LibroServiceImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Libro>> getLibros() {
        var encontrados=repository.obtenerTodosLosLibros();
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Libro>> getLibrosPorBusquedaGral(String keyword) {
        var encontrados=repository.obtenerLibrosPorBusquedaGeneral(keyword);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Libro>> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        var encontrados=repository.obtenerLibrosPorBusquedaAvz(keyword, campoABuscar);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public Optional<Libro> guardarNuevoLibro(Libro aGuardar) {
        if (!existeLibroConMismoTituloYAutor(aGuardar.getTitulo(), aGuardar.getAutor()))
            return repository.crearNuevoLibro(aGuardar);
        else
            throw new DuplicatedBookException(aGuardar.getTitulo(), aGuardar.getAutor());
    }

    @Override
    public boolean existeLibroConMismoTituloYAutor(String titulo, String autor) {
        var existenteEnLaBase=repository.buscarLibroPorTituloYAutor(titulo,autor);
        String existe=existenteEnLaBase.orElseGet(Libro::new).getTitulo();
        return null!=existe;
    }

    @Override
    public Optional<Libro> editarLibro(Libro editadoSinCodigo, String codigo) {
        if (existeLibroConMismoTituloYAutor(editadoSinCodigo.getTitulo(), editadoSinCodigo.getAutor()))
            return repository.editarLibroExistente(editadoSinCodigo,codigo);
        else
            throw new BookNotFoundException(editadoSinCodigo.getCodigo());
    }
}
