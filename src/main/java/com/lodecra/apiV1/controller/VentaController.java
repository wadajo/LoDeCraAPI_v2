package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class VentaController {

    private final VentaService ventaService;

    private final EjemplarService ejemplarService;

    private final VentaMapper ventaMapper;

    private final EjemplarMapper ejemplarMapper;

    public VentaController(VentaService ventaService, EjemplarService ejemplarService, VentaMapper ventaMapper, EjemplarMapper ejemplarMapper) {
        this.ventaService = ventaService;
        this.ejemplarService = ejemplarService;
        this.ventaMapper = ventaMapper;
        this.ejemplarMapper = ejemplarMapper;
    }

    @PostMapping("/ventas/{codLibro}/ejemplar/{nroEjemplar}")
    public ResponseEntity<VentaDto> hacerVentaRapida (@PathVariable String codLibro, @PathVariable Integer nroEjemplar){
        try{
            log.info("Llamando a POST /ventas para libro de código "+codLibro+" y ejemplar nro. "+nroEjemplar);
            var ventaHecha=ventaService.hacerVentaRapida(codLibro,nroEjemplar);
            log.info("Hecha la venta en fecha y hora: "+ventaHecha.getFechaHoraVendido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())));
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.ventaToVentaDto(ventaHecha));
        } catch (WrongVolumeNoException e) {
            log.error("No se encontró el volumen "+nroEjemplar+" del libro con código "+codLibro);
            throw e;
        } catch (VolumeAlreadySoldException e) {
            log.error("El volumen "+nroEjemplar+" del libro con código "+codLibro+" ya fue vendido con anterioridad.");
            throw e;
        }
    }

}

