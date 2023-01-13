package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.service.port.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class VentaController {

    private final VentaService ventaService;

    private final VentaMapper mapper;

    public VentaController(VentaService ventaService, VentaMapper mapper) {
        this.ventaService = ventaService;
        this.mapper = mapper;
    }

    @PostMapping("/ventas/{codLibro}/ejemplar/{nroEjemplar}")
    public ResponseEntity<VentaDto> hacerVentaRapida (@PathVariable String codLibro, @PathVariable Integer nroEjemplar){
        try{
            ventaService.hacerVentaRapida(codLibro,nroEjemplar);
            var ventaHecha=ventaService.obtenerVenta(codLibro,nroEjemplar);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.ventaToVentaDto(ventaHecha));
        } catch (WrongVolumeNoException e) {
            log.error("No se encontró el volumen "+nroEjemplar+" del libro con código "+codLibro);
            throw e;
        }
    }

}

