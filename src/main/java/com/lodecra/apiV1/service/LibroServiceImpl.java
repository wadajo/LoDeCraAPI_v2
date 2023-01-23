package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.DuplicatedBookException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
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
    public Optional<List<Libro>> getLibrosDisponibles() {
        var encontrados=repository.obtenerTodosLosLibrosDisponibles();
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    public Optional<List<Libro>> getLibrosPorBusquedaGral(String keyword) {
        var encontrados=repository.obtenerLibrosPorBusquedaGeneral(keyword);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    public Optional<List<Libro>> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
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
    public Optional<Libro> editarLibro(Libro editadoSinCodigo, String codigo) {
        if (getLibroPorCodigo(codigo).isPresent())
            return repository.editarLibroExistente(editadoSinCodigo,codigo);
        else
            throw new BookNotFoundException(codigo);
    }

    @Transactional
    @Override
    public void descartarLibro(String codigo) {
        if (getLibroPorCodigo(codigo).isPresent())
            repository.descartarLibro(codigo);
        else
            throw new BookNotFoundException(codigo);
    }
}
