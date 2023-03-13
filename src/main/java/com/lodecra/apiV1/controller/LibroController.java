package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import com.lodecra.apiV1.util.Utilidades;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j(topic = "LoDeCraLogger")
@Validated
public class LibroController extends BaseController {

    private final LibroService libroService;

    private final ConversionService cs;

    private final Utilidades util;

    public LibroController(LibroService libroService, ConversionService cs, Utilidades util) {
        this.libroService = libroService;
        this.cs = cs;
        this.util = util;
    }

    @GetMapping("/libros")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookDto>> librosDisponibles
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) String campoABuscar){
        List<Libro> todosLosLibros;
        try {
            log.info("Usuario autenticado: "+util.usuarioAutenticado());
            if(null!=keyword && null!=campoABuscar) {
                log.info("Llamando a GET /libros con búsqueda avanzada por "+campoABuscar+". Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosPorBusquedaAvz(keyword, campoABuscar);
            } else if(null != keyword) {
                log.info("Llamando a GET /libros con búsqueda general. Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosDisponiblesPorBusquedaGral(keyword);
            } else {
                log.info("Llamando a GET /libros.");
                todosLosLibros = libroService.getLibrosDisponibles();
            }
        } catch (EmptySearchException e) {
            log.error("No se encontraron libros disponibles.");
            throw e;
        }
        log.info("Devolviendo "+todosLosLibros.size()+" libros disponibles.");
        List<BookDto> todosLosLibrosDto = new ArrayList<>();
        todosLosLibros.forEach(libro->todosLosLibrosDto.add(cs.convert(libro,BookDto.class)));
        return ResponseEntity.ok(todosLosLibrosDto);
    }

    @GetMapping("/libros/{codLibro}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Optional<BookDto>> libroPorCodigo(@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro){
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a GET /libros/{codigo} para libro con código "+codLibro);
        Optional<Libro> aDevolver;
        BookDto libroEncontrado = null;
        try {
            aDevolver = libroService.getLibroPorCodigo(codLibro);
            if (aDevolver.isPresent()) {
                log.info("Encontrado libro con código "+codLibro);
                libroEncontrado = cs.convert(aDevolver.get(), BookDto.class);
            }
            return ResponseEntity.ok(Optional.ofNullable(libroEncontrado));
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
    }

    @PostMapping("/libros")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> nuevoLibro(@RequestBody @Valid BookDto nuevo) {
        String titulo = nuevo.name();
        String autor = nuevo.author();
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a POST /libros con nuevo libro. Título: "+titulo+". Autor/a: "+autor+".");
        Optional<Libro> guardado;
        try {
            guardado = libroService.guardarNuevoLibro(cs.convert(nuevo,Libro.class));
            var aDevolver = cs.convert(guardado,BookDto.class);
            if (null!=aDevolver) {
                log.info("Guardado nuevo libro. Título: " + titulo + ". Autor/a: " + autor + ". Código: " + aDevolver.code());
                return ResponseEntity.created(URI.create("/api/lodecra/v2/libros/" + aDevolver.code())).body(aDevolver);
            } else {
                throw new BookNotSavedException(titulo);
            }
        } catch (BookNotSavedException | DuplicatedBookException e) {
            log.error("No se puedo guardar el libro con título "+titulo+" y autor "+autor);
            throw e;
        }
    }

    @PutMapping("/libros/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> editarLibro(@RequestBody @Valid BookDto editado, @Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro) {
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a PUT /libros/{codigo} con libro de código "+codLibro);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codLibro);
            if(libroExistente.isPresent()) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                var guardado = libroService.editarLibro(cs.convert(editado,Libro.class),codLibro);
                var aDevolver = cs.convert(guardado,BookDto.class);
                if(null!=aDevolver) {
                    log.info("Actualizado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codLibro);
                    return ResponseEntity.ok(aDevolver);
                } else {
                    throw new BookNotFoundException(codLibro);
                }
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con el código "+codLibro);
            throw e;
        }
    }

    @DeleteMapping("/libros/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> borrarLibro(@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro) {
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a DELETE /libros/{codigo} con libro de código "+codLibro);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codLibro);
            if(libroExistente.isPresent() && (null==libroExistente.get().getDescartado() || !libroExistente.get().getDescartado())) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                libroService.descartarLibro(codLibro);
                var aDevolver = cs.convert(libroService.getLibroPorCodigo(codLibro),BookDto.class);
                if(null!=aDevolver) {
                    log.info("Descartado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codLibro);
                    return ResponseEntity.ok(aDevolver);
                } else {
                    throw new BookNotFoundException(codLibro);
                }

            } else if (libroExistente.isPresent()) {
                throw new BookAlreadyDiscardedException(libroExistente.get().getTitulo());
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con el código "+codLibro);
            throw e;
        } catch (BookAlreadyDiscardedException e) {
            log.error("El libro de código "+codLibro+" ya está descartado.");
            throw e;
        }
    }

}

