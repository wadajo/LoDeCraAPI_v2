package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j(topic = "LoDeCraLogger")
@Validated
public class LibroController extends BaseController {

    private final LibroService libroService;

    private final LibroMapper mapper;

    public LibroController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @GetMapping("/libros")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookDto>> librosDisponibles
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) String campoABuscar){
        List<Libro> todosLosLibros;
        try {
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
        var todosLosLibrosDto = todosLosLibros.stream().map(mapper::libroToBookDto).toList();
        return ResponseEntity.ok(todosLosLibrosDto);
    }

    @GetMapping("/libros/{codLibro}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Optional<BookDto>> libroPorCodigo(@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro){
        log.info("Llamando a GET /libros/{codigo} para libro con código "+codLibro);
        Optional<Libro> aDevolver;
        try {
            aDevolver = libroService.getLibroPorCodigo(codLibro);
            log.info("Encontrado libro con código "+codLibro);
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.ok(aDevolver.map(mapper::libroToBookDto));
    }

    @PostMapping("/libros")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> nuevoLibro(@RequestBody @Valid BookDto nuevo) {
        String titulo = nuevo.name();
        String autor = nuevo.author();
        log.info("Llamando a POST /libros con nuevo libro. Título: "+titulo+". Autor: "+autor+".");
        Optional<Libro> guardado;
        try {
            guardado = libroService.guardarNuevoLibro(mapper.bookDtoToLibro(nuevo));
            var aDevolver = mapper.libroToBookDto(guardado.orElseThrow());
            log.info("Guardado nuevo libro. Título: "+titulo+". Autor: "+autor+". Código: "+aDevolver.code());
            return ResponseEntity.status(HttpStatus.CREATED).body(aDevolver);
        } catch (BookNotSavedException | DuplicatedBookException e) {
            log.error("No se puedo guardar el libro con título "+titulo+" y autor "+autor);
            throw e;
        }
    }

    @PutMapping("/libros/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> editarLibro(@RequestBody @Valid BookDto editado, @Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro) {
        log.info("Llamando a PUT /libros/{codigo} con libro de código "+codLibro);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codLibro);
            if(libroExistente.isPresent()) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                var guardado = libroService.editarLibro(mapper.bookDtoToLibro(editado),codLibro);
                BookDto aDevolver = mapper.libroToBookDto(guardado.orElseThrow(()-> new BookNotFoundException(codLibro)));
                log.info("Actualizado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codLibro);
                return ResponseEntity.ok(aDevolver);
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
        log.info("Llamando a DELETE /libros/{codigo} con libro de código "+codLibro);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codLibro);
            if(libroExistente.isPresent() && (null==libroExistente.get().getDescartado() || !libroExistente.get().getDescartado())) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                libroService.descartarLibro(codLibro);
                BookDto aDevolver = mapper.libroToBookDto(libroService.getLibroPorCodigo(codLibro).orElseThrow(()-> new BookNotFoundException(codLibro)));
                log.info("Descartado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codLibro);
                return ResponseEntity.ok(aDevolver);
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

