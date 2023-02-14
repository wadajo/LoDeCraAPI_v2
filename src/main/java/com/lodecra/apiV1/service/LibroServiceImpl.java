package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.DuplicatedBookException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.LibroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class LibroServiceImpl implements LibroService {

    LibroRepository repository;

    public LibroServiceImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Libro> getLibrosDisponibles() throws EmptySearchException{
        var encontrados=repository.obtenerTodosLosLibrosDisponibles();
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados.get();
        }
    }

    @Override
    public List<Libro> getLibrosDisponiblesPorBusquedaGral(String keyword) {
        var encontrados=repository.obtenerLibrosDisponiblesPorBusquedaGeneral(keyword);
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
    public Optional<Libro> getLibroPorCodigo(String codLibro) {
        Optional<Libro> encontrado=repository.obtenerLibroPorCodigo(codLibro);
        if (encontrado.isPresent())
            return encontrado;
        else
            throw new BookNotFoundException(codLibro);
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

    @Transactional
    @Override
    public Optional<Libro> editarLibro(Libro editadoSinCodigo, String codLibro) {
        if (getLibroPorCodigo(codLibro).isPresent())
            return repository.editarLibroExistente(editadoSinCodigo,codLibro);
        else
            throw new BookNotFoundException(codLibro);
    }

    @Transactional
    @Override
    public void descartarLibro(String codLibro) {
        if (getLibroPorCodigo(codLibro).isPresent())
            repository.descartarLibro(codLibro);
        else
            throw new BookNotFoundException(codLibro);
    }
}
