package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class EjemplarController {

    private final EjemplarService ejemplarService;

    private final EjemplarMapper mapper;

    public EjemplarController(EjemplarService ejemplarService, EjemplarMapper mapper) {
        this.ejemplarService = ejemplarService;
        this.mapper = mapper;
    }

    @GetMapping("/ejemplares/{codLibro}")
    public ResponseEntity<List<EjemplarDto>> ejemplaresDelLibro(@PathVariable String codLibro){
        log.info("Llamando a GET /ejemplares/{codLibro} para libro con código "+codLibro);
        Optional<List<Ejemplar>> aDevolverOptional;
        List<EjemplarDto> aDevolver=new ArrayList<>();
        try {
            aDevolverOptional = ejemplarService.getEjemplaresPorCodigoLibro(codLibro);
            log.info("Encontrados "+aDevolverOptional.orElseGet(Collections::emptyList).size()+" ejemplares de este libro.");
            aDevolverOptional.ifPresent(lista->lista.forEach(ejemplar -> aDevolver.add(mapper.ejemplarToEjemplarDto(ejemplar))));
        } catch (WrongIdFormatException e) {
            log.error("El código "+codLibro+" tiene un formato erróneo");
            throw e;
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.ok(aDevolver);
    }

}

