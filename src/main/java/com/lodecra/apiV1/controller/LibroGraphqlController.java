package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

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
        return listaDeLibrosAFluxDeDtos(libroService.getLibrosDisponibles());
    }

    @QueryMapping
    public Flux<BookDto> booksContainingKeyword(@Argument String keyword){
        return listaDeLibrosAFluxDeDtos(libroService.getLibrosDisponiblesPorBusquedaGral(keyword));
    }

    private Flux<BookDto> listaDeLibrosAFluxDeDtos(List<Libro> listaDeLibros){
        var listaDeDtos=new ArrayList<BookDto>();
        listaDeLibros.forEach(libro-> listaDeDtos.add(mapper.libroToBookDto(libro)));
        return Flux.fromIterable(listaDeDtos);
    }

}

