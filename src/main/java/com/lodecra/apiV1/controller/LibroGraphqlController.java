package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.exception.EmptySearchException;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@Controller
@Slf4j
public class LibroGraphqlController {

    private final LibroService libroService;

    private final LibroMapper mapper;

    public LibroGraphqlController(LibroService libroService, LibroMapper mapper) {
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @QueryMapping
    public Flux<BookDto> booksAvailable(){
        var todosLosLibrosDto=new ArrayList<BookDto>();
        var librosDisponibles=libroService.getLibrosDisponibles().orElseThrow(EmptySearchException::new);
        librosDisponibles.forEach(libro-> todosLosLibrosDto.add(mapper.libroToBookDto(libro)));
        return Flux.fromIterable(todosLosLibrosDto);
    }


}

