package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final LibroService libroService;

    private final EjemplarMapper mapper;

    public EjemplarController(EjemplarService ejemplarService, LibroService libroService, EjemplarMapper mapper) {
        this.ejemplarService = ejemplarService;
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @GetMapping("/ejemplares/{codLibro}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EjemplarDto>> ejemplaresDelLibro(@PathVariable String codLibro){
        log.info("Llamando a GET /ejemplares/{codLibro} para libro con código "+codLibro);
        Optional<List<Ejemplar>> aDevolverOptional;
        List<EjemplarDto> aDevolver=new ArrayList<>();
        try {
            var libroBuscado = libroService.getLibroPorCodigo(codLibro);
            aDevolverOptional = ejemplarService.getEjemplaresPorCodigoLibro(codLibro);
            log.info("Encontrados "+aDevolverOptional.orElseGet(Collections::emptyList).size()+" ejemplares de este libro.");
            if(aDevolverOptional.isPresent() && libroBuscado.isPresent()){
                var listaDeEjemplares=aDevolverOptional.get();
                var libroCorrespondiente=libroBuscado.get();
                listaDeEjemplares.forEach(ejemplar -> aDevolver.add(mapper.ejemplarAndLibroToEjemplarDto(ejemplar,libroCorrespondiente)));
            }
        } catch (WrongIdFormatException e) {
            log.error("El código "+codLibro+" tiene un formato erróneo");
            throw e;
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.ok(aDevolver);
    }

    @PostMapping("/ejemplares/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EjemplarDto> agregarEjemplarNuevo (
            @PathVariable String codLibro,
            @RequestParam String modalidad,
            @RequestParam String ubicacion) {
        log.info("Llamando a POST /ejemplares/"+codLibro+" para nuevo ejemplar.");
        Ejemplar guardado;
        try{
            guardado=ejemplarService.guardarNuevoEjemplar(codLibro,ubicacion,modalidad);
            log.info("Guardado el ejemplar nro. "+guardado.getNroEjemplar()+" de libro de código "+guardado.getLibro().getCodigo());
        } catch (WrongIdFormatException e) {
            log.error("El código "+codLibro+" tiene un formato erróneo");
            throw e;
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.ejemplarAndLibroToEjemplarDto(guardado,guardado.getLibro()));
    }

}

