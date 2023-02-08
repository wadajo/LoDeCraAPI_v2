package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.service.port.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final ConversionService conversionService;

    public VentaController(VentaService ventaService, ConversionService conversionService) {
        this.ventaService = ventaService;
        this.conversionService = conversionService;
    }

    @GetMapping("/ventas/{codLibro}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<VentaDto>> mostrarVentasDelLibro (@PathVariable String codLibro) {
        log.info("Llamando a GET /ventas para libro de código "+codLibro);
        List<VentaDto> ventasDto=new ArrayList<>();
        try {
            ventaService.listarVentasDelLibro(codLibro).forEach(unaVenta->ventasDto.add(conversionService.convert(unaVenta,VentaDto.class)));
            if (ventasDto.isEmpty()) {
                log.error("No se encontraron ventas de este libro.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            else {
                log.info("Devolviendo "+ventasDto.size()+" ventas encontradas de este libro.");
                return ResponseEntity.ok(ventasDto);
            }
        } catch (WrongIdFormatException e) {
            log.error("El código "+codLibro+" tiene un formato erróneo");
            throw e;
        }
    }
    @PostMapping("/ventas/{codLibro}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaDto> hacerVentaRapida (@PathVariable String codLibro, @RequestParam Integer nroEjemplar){
        try{
            log.info("Llamando a POST /ventas para libro de código "+codLibro+" y ejemplar nro. "+nroEjemplar);
            var ventaHecha=ventaService.hacerVentaRapida(codLibro,nroEjemplar);
            log.info("Hecha la venta en fecha y hora: "+ventaHecha.getFechaHoraVendido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())));
            return ResponseEntity.status(HttpStatus.CREATED).body(conversionService.convert(ventaHecha,VentaDto.class));
        } catch (WrongIdFormatException e) {
            log.error("El código "+codLibro+" tiene un formato erróneo");
            throw e;
        } catch (WrongVolumeNoException e) {
            log.error("No se encontró el volumen "+nroEjemplar+" del libro con código "+codLibro);
            throw e;
        } catch (VolumeAlreadySoldException e) {
            log.error("El volumen "+nroEjemplar+" del libro con código "+codLibro+" ya fue vendido con anterioridad.");
            throw e;
        }
    }

}

