package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.DuplicatedBookException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.LibroService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@Observed(name = "service-libros")
public class LibroServiceImpl implements LibroService {

    private final LibroRepository repository;

    public LibroServiceImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    @Observed(name = "libros-disponibles")
    public List<Libro> getLibrosDisponibles() throws EmptySearchException{
        var encontrados=repository.obtenerTodosLosLibrosDisponibles();
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados.get();
        }
    }

    @Override
    @Observed(name = "libros-busqueda-gral")
    public List<Libro> getLibrosDisponiblesPorBusquedaGral(String keyword) {
        var encontrados=repository.obtenerLibrosDisponiblesPorBusquedaGeneral(keyword);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    @Observed(name = "libros-busqueda-avz")
    public List<Libro> getLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        var encontrados=repository.obtenerLibrosPorBusquedaAvz(keyword, campoABuscar);
        if (encontrados.isEmpty()){
            throw new EmptySearchException();
        } else {
            return encontrados;
        }
    }

    @Override
    @Observed(name = "libro-por-codigo")
    public Optional<Libro> getLibroPorCodigo(String codLibro) {
        Optional<Libro> encontrado=repository.obtenerLibroPorCodigo(codLibro);
        if (encontrado.isPresent())
            return encontrado;
        else
            throw new BookNotFoundException(codLibro);
    }

    @Transactional
    @Override
    @Observed(name = "nuevo-libro")
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
    @Observed(name = "editar-libro")
    public Optional<Libro> editarLibro(Libro editadoSinCodigo, String codLibro) {
        if (getLibroPorCodigo(codLibro).isPresent())
            return repository.editarLibroExistente(editadoSinCodigo,codLibro);
        else
            throw new BookNotFoundException(codLibro);
    }

    @Transactional
    @Override
    @Observed(name = "descartar-libro")
    public void descartarLibro(String codLibro) {
        if (getLibroPorCodigo(codLibro).isPresent())
            repository.descartarLibro(codLibro);
        else
            throw new BookNotFoundException(codLibro);
    }
}
