package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class LibroController {

    private final LibroService libroService;

    private final LibroMapper mapper;

    public LibroController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @GetMapping("/libros")
    public ResponseEntity<List<BookDto>> librosDisponibles
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) String campoABuscar){
        List<Libro> todosLosLibros;
        try {
            if(null!=keyword && null!=campoABuscar) {
                log.info("Llamando a GET /libros con búsqueda avanzada por "+campoABuscar+". Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosPorBusquedaAvz(keyword, campoABuscar).orElseThrow(EmptySearchException::new);
            } else if(null != keyword) {
                log.info("Llamando a GET /libros con búsqueda general. Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosPorBusquedaGral(keyword).orElseThrow(EmptySearchException::new);
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

    @GetMapping("/libros/{codigo}")
    public ResponseEntity<Optional<BookDto>> libroPorCodigo(@PathVariable String codigo){
        log.info("Llamando a GET /libros/{codigo} para libro con código "+codigo);
        Optional<Libro> aDevolver;
        try {
            aDevolver = libroService.getLibroPorCodigo(codigo);
            log.info("Encontrado libro con código "+codigo);
        } catch (WrongIdFormatException e) {
            log.error("El código "+codigo+" tiene un formato erróneo");
            throw e;
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codigo);
            throw e;
        }
        return ResponseEntity.ok(aDevolver.map(mapper::libroToBookDto));
    }

    @PostMapping("/libros")
    public ResponseEntity<BookDto> nuevoLibro(@RequestBody Libro nuevo) {
        String titulo = nuevo.getTitulo();
        String autor = nuevo.getAutor();
        log.info("Llamando a POST /libros con nuevo libro. Título: "+titulo+". Autor: "+autor+".");
        Optional<Libro> guardado;
        try {
            guardado = libroService.guardarNuevoLibro(nuevo);
            BookDto aDevolver = mapper.libroToBookDto(guardado.orElseGet(Libro::new));
            log.info("Guardado nuevo libro. Título: "+titulo+". Autor: "+autor+". Código: "+aDevolver.code());
            return ResponseEntity.status(HttpStatus.CREATED).body(aDevolver);
        } catch (BookNotSavedException | DuplicatedBookException e) {
            log.error("No se guardó el libro con título "+titulo+" y autor "+autor);
            throw e;
        }
    }

    @PutMapping("/libros/{codigo}")
    public ResponseEntity<BookDto> editarLibro(@RequestBody Libro editado, @PathVariable String codigo) {
        log.info("Llamando a PUT /libros/{codigo} con libro de código "+codigo);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codigo);
            if(libroExistente.isPresent()) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                var guardado = libroService.editarLibro(editado,codigo);
                BookDto aDevolver = mapper.libroToBookDto(guardado.orElseThrow(()-> new BookNotFoundException(codigo)));
                log.info("Actualizado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codigo);
                return ResponseEntity.ok(aDevolver);
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con el código "+codigo);
            throw e;
        }
    }

    @DeleteMapping("/libros/{codigo}")
    public ResponseEntity<BookDto> borrarLibro(@PathVariable String codigo) {
        log.info("Llamando a DELETE /libros/{codigo} con libro de código "+codigo);
        try {
            var libroExistente = libroService.getLibroPorCodigo(codigo);
            if(libroExistente.isPresent() && (null==libroExistente.get().getDescartado() || !libroExistente.get().getDescartado())) {
                log.info("Encontrado en la Base. Título: "+libroExistente.get().getTitulo());
                libroService.descartarLibro(codigo);
                BookDto aDevolver = mapper.libroToBookDto(libroService.getLibroPorCodigo(codigo).orElseThrow(()-> new BookNotFoundException(codigo)));
                log.info("Descartado libro. Título: " + aDevolver.name() + ". Autor: " + aDevolver.author() + ". Código: " + codigo);
                return ResponseEntity.ok(aDevolver);
            } else if (libroExistente.isPresent()) {
                throw new BookAlreadyDiscardedException(libroExistente.get().getTitulo());
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con el código "+codigo);
            throw e;
        } catch (BookAlreadyDiscardedException e) {
            log.error("El libro de código "+codigo+" ya está descartado.");
            throw e;
        }
    }

}

