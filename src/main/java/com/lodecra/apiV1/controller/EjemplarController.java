package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.LibroService;
import com.lodecra.apiV1.util.Utilidades;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j(topic = "LoDeCraLogger")
@Validated
public class EjemplarController extends BaseController {

    private final EjemplarService ejemplarService;

    private final LibroService libroService;

    private final EjemplarMapper mapper;

    private final Utilidades util;

    public EjemplarController(EjemplarService ejemplarService, LibroService libroService, EjemplarMapper mapper, Utilidades util) {
        this.ejemplarService = ejemplarService;
        this.libroService = libroService;
        this.mapper = mapper;
        this.util = util;
    }

    @GetMapping("/ejemplares/{codLibro}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EjemplarDto>> ejemplaresDelLibro(@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro){
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a GET /ejemplares/{codLibro} para libro con código "+codLibro);
        List<Ejemplar> aDevolverEjemplar;
        List<EjemplarDto> aDevolver=new ArrayList<>();
        try {
            var libroBuscado = libroService.getLibroPorCodigo(codLibro);
            aDevolverEjemplar = ejemplarService.getEjemplaresDisponiblesPorCodigo(codLibro);
            log.info("Encontrados "+aDevolverEjemplar.size()+" ejemplares de este libro.");
            if(!aDevolverEjemplar.isEmpty() && libroBuscado.isPresent()){
                var libroCorrespondiente=libroBuscado.get();
                aDevolverEjemplar.forEach(ejemplar -> aDevolver.add(mapper.ejemplarAndLibroToEjemplarDto(ejemplar,libroCorrespondiente)));
            }
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.ok(aDevolver);
    }

    @PostMapping("/ejemplares/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EjemplarDto> agregarEjemplarNuevo (
            @Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable String codLibro,
            @RequestParam("modality") String modalidad,
            @RequestParam("location") String ubicacion) {
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a POST /ejemplares/"+codLibro+" para nuevo ejemplar.");
        Ejemplar guardado;
        try{
            guardado=ejemplarService.guardarNuevoEjemplar(codLibro,ubicacion,modalidad);
            log.info("Guardado el ejemplar nro. "+guardado.getNroEjemplar()+" de libro de código "+guardado.getLibro().getCodigo());
        } catch (BookNotFoundException e) {
            log.error("No se encontró el libro con código "+codLibro);
            throw e;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.ejemplarAndLibroToEjemplarDto(guardado,guardado.getLibro()));
    }

}

