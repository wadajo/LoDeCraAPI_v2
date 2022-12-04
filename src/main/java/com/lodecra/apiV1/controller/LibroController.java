package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.entity.Libro;
import com.lodecra.apiV1.mapper.LibroMapper;
import com.lodecra.apiV1.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LibroController {

    LibroService libroService;

    LibroMapper mapper;

    public LibroController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @GetMapping("/libros")
    public ResponseEntity<List<LibroDto>> librosDisponibles
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) String campoABuscar){
        List<Libro> todosLosLibros;
        if(null!=keyword && null!=campoABuscar)
            todosLosLibros=libroService.getLibrosPorBusquedaAvz(keyword,campoABuscar);
        else if(null != keyword)
            todosLosLibros=libroService.getLibrosPorBusquedaGral(keyword);
        else
            todosLosLibros=libroService.getLibros();

        var librosDto = todosLosLibros.stream().map(mapper::libroADto).toList();
        return ResponseEntity.ok().body(librosDto);
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<LibroDto> libroPorId(@PathVariable String id){
        var unLibro = libroService.getLibroPorId(id);
        var libroDto = mapper.libroADto(unLibro);
        return ResponseEntity.ok().body(libroDto);
    }

}

