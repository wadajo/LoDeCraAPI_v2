package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.LibroService;
import com.lodecra.apiV1.service.port.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class VentaController {

    private final VentaService ventaService;

    private final EjemplarService ejemplarService;

    private final LibroService libroService;

    private final VentaMapper mapper;

    public VentaController(VentaService ventaService, EjemplarService ejemplarService, LibroService libroService, VentaMapper mapper) {
        this.ventaService = ventaService;
        this.ejemplarService = ejemplarService;
        this.libroService = libroService;
        this.mapper = mapper;
    }

    @PostMapping("/ventas/{codLibro}/ejemplar/{nroEjemplar}")
    public ResponseEntity<VentaDto> hacerVentaRapida (@PathVariable String codLibro, @PathVariable Integer nroEjemplar){
        try{
            var ejemplarAVender=ejemplarService.getEjemplarNro(codLibro, nroEjemplar);
            var ventaAHacer=new Venta(
                    ejemplarAVender.orElseThrow(),
                    LocalDateTime.now(ZoneId.systemDefault()),
                    ejemplarAVender.get().getLibro().getPrecio());
            ventaService.hacerVentaRapida(ventaAHacer);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.ventaToVentaDto(ventaAHacer));
        } catch (WrongVolumeNoException e) {
            log.error("No se encontró el volumen "+nroEjemplar+" del libro con código "+codLibro);
            throw e;
        }
    }

}

