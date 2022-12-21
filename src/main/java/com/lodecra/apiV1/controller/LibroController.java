package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("/lodecra/api/2.0")
@Slf4j
public class LibroController {

    private final LibroService libroService;

    private final LibroMapper mapper;

    public LibroController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @GetMapping("/libros")
    public ResponseEntity<List<LibroDto>> librosDisponibles
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) String campoABuscar){
        List<Libro> todosLosLibros;
        try {
            if(null!=keyword && null!=campoABuscar) {
                log.info("Llamando a GET /libros con búsqueda avanzada por "+campoABuscar+". Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosPorBusquedaAvz(keyword, campoABuscar);
            } else if(null != keyword) {
                log.info("Llamando a GET /libros con búsqueda general. Keyword: " + keyword);
                todosLosLibros = libroService.getLibrosPorBusquedaGral(keyword);
            } else {
                log.info("Llamando a GET /libros.");
                todosLosLibros = libroService.getLibros();
            }
        } catch (EmptySearchException e) {
            log.info("No se encontraron libros.");
            throw e;
        }
        var librosDto = todosLosLibros.stream().map(mapper::libroToLibroDto).toList();
        log.info("Devolviendo "+librosDto.size()+" libros.");
        return ResponseEntity.ok(librosDto);
    }

    @GetMapping("/libros/{codigo}")
    public ResponseEntity<Optional<LibroDto>> libroPorCodigo(@PathVariable String codigo){
        log.info("Llamando a GET /libros/{codigo} para libro con código "+codigo);
        Optional<Libro> aDevolver;
        try {
            aDevolver = libroService.getLibroPorCodigo(codigo);
            log.info("Encontrado libro con código "+codigo);
        } catch (WrongIdFormatException e) {
            log.info("El código "+codigo+"tiene un formato erróneo");
            throw e;
        } catch (BookNotFoundException e) {
            log.info("No se encontró el libro con código "+codigo);
            throw e;
        }
        return ResponseEntity.ok(aDevolver.map(mapper::libroToLibroDto));
    }

}

