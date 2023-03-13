package com.lodecra.apiV1.controller;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.exception.WrongVolumeNoException;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.service.port.VentaService;
import com.lodecra.apiV1.util.Utilidades;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/lodecra/v2")
@Slf4j(topic = "LoDeCraLogger")
@Validated
public class VentaController extends BaseController {

    private final VentaService ventaService;

    private final ConversionService cs;

    private final Utilidades util;

    public VentaController(VentaService ventaService, ConversionService cs, Utilidades util) {
        this.ventaService = ventaService;
        this.cs = cs;
        this.util = util;
    }

    @GetMapping("/sales/{code}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<VentaDto>> mostrarVentasDelLibro (@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable("code") String codLibro) {
        log.info("Usuario autenticado: "+util.usuarioAutenticado());
        log.info("Llamando a GET /sales para libro de código "+codLibro);
        List<VentaDto> ventasDto=new ArrayList<>();
        ventaService.listarVentasDelLibro(codLibro).forEach(unaVenta->ventasDto.add(cs.convert(unaVenta,VentaDto.class)));
        if (ventasDto.isEmpty()) {
            log.error("No se encontraron ventas de este libro.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            log.info("Devolviendo "+ventasDto.size()+" ventas encontradas de este libro.");
            return ResponseEntity.ok(ventasDto);
        }
    }
    @PostMapping("/sales/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaDto> hacerVenta(@Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format")@PathVariable("code") String codLibro,
                                               @RequestParam("volumeNum") Integer nroEjemplar,
                                               @RequestParam(required = false, name = "price") Integer precioVendido,
                                               @RequestParam(required = false, name = "dateSold")  LocalDateTime fechaHoraVendido){
        try{
            log.info("Usuario autenticado: "+util.usuarioAutenticado());
            log.info("Llamando a POST /sales para libro de código "+codLibro+" y ejemplar nro. "+nroEjemplar);
            if(null==precioVendido&&null==fechaHoraVendido) {
                log.info("Venta rápida a precio de lista y fecha actual.");
            } else if(null==precioVendido) {
                log.info("Venta especial, en fecha: "+fechaHoraVendido.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault()))+".");
            } else if(null==fechaHoraVendido) {
                log.info("Venta especial al precio: "+precioVendido+" pesos.");
            }
                Venta hecha=ventaService.hacerVenta(codLibro, nroEjemplar, precioVendido, fechaHoraVendido);
                log.info("Hecha la venta en fecha y hora: " + hecha.getFechaHoraVendido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())));
                return ResponseEntity.status(HttpStatus.CREATED).body(cs.convert(hecha, VentaDto.class));
        } catch (WrongVolumeNoException e) {
            log.error("No se encontró el volumen "+nroEjemplar+" del libro con código "+codLibro);
            throw e;
        } catch (VolumeAlreadySoldException e) {
            log.error("El volumen "+nroEjemplar+" del libro con código "+codLibro+" ya fue vendido con anterioridad.");
            throw e;
        }
    }

}

