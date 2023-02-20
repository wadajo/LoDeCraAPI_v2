package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class LibroGraphqlController {

    private final LibroService libroService;

    private final ConversionService cs;

    public LibroGraphqlController(LibroService libroService, ConversionService cs) {
        this.libroService = libroService;
        this.cs = cs;
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public Flux<BookDto> booksAvailable(){
        return listaDeLibrosAFluxDeDtos(libroService.getLibrosDisponibles());
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public Flux<BookDto> booksContainingKeyword(@Argument String keyword){
        return listaDeLibrosAFluxDeDtos(libroService.getLibrosDisponiblesPorBusquedaGral(keyword));
    }

    private Flux<BookDto> listaDeLibrosAFluxDeDtos(List<Libro> listaDeLibros){
        List<BookDto> listaDeDtos=new ArrayList<>();
        listaDeLibros.forEach(libro-> listaDeDtos.add(cs.convert(libro,BookDto.class)));
        return Flux.fromIterable(listaDeDtos);
    }

}

