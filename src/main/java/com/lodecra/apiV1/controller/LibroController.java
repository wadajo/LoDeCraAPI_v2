package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.mapper.LibroMapper;
import com.lodecra.apiV1.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<LibroDto>> librosDisponibles(){
        var todosLosLibros = libroService.getLibros();
        var librosDto = todosLosLibros.stream().map(mapper::libroADto).toList();
        return ResponseEntity.ok().body(librosDto);
    }

}

