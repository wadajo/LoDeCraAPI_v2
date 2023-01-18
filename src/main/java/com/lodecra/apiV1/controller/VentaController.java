package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.service.port.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j
public class VentaController {

    private final VentaService ventaService;

    private final VentaMapper ventaMapper;

    public VentaController(VentaService ventaService, VentaMapper ventaMapper) {
        this.ventaService = ventaService;
        this.ventaMapper = ventaMapper;
    }

    @PostMapping("/ventas/{codLibro}")
    public ResponseEntity<VentaDto> hacerVentaRapida (@PathVariable String codLibro, @RequestParam Integer nroEjemplar){
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

    @GetMapping("/ventas/{codLibro}")
    public ResponseEntity<List<VentaDto>> mostrarVentasDelLibro (@PathVariable String codLibro) {
        log.info("Llamando a GET /ventas para libro de código "+codLibro);
        List<VentaDto> ventasDto=new ArrayList<>();
        ventaService.listarVentasDelLibro(codLibro).forEach(unaVenta->ventasDto.add(ventaMapper.ventaToVentaDto(unaVenta)));
        if (ventasDto.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.ok(ventasDto);
    }

}

