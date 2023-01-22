package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class LibroGraphqlController {

    private final LibroService libroService;

    private final LibroMapper mapper;

    public LibroGraphqlController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @QueryMapping
    public Flux<LibroDto> librosDisponibles(){
        var todosLosLibros=new ArrayList<Libro>();
        var todosLosLibrosDto=new ArrayList<LibroDto>();

        return Flux.fromIterable(todosLosLibrosDto);
    }


}

