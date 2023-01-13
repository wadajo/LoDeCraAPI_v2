package com.lodecra.apiV1.service;

import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.port.VentaRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Transactional(readOnly = true)
@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    private final EjemplarService ejemplarService;

    public VentaServiceImpl(VentaRepository ventaRepository, EjemplarService ejemplarService) {
        this.ventaRepository = ventaRepository;
        this.ejemplarService = ejemplarService;
    }


    @Override
    public void hacerVentaRapida(String codLibro, Integer nroEjemplar) {
        ventaRepository.saveVenta(crearVentaAhora(codLibro, nroEjemplar));
    }

    private Venta crearVentaAhora(String codLibro, Integer nroEjemplar) {
        var ejemplarExistenteOptional=ejemplarService.getEjemplarNro(codLibro, nroEjemplar);
        Ejemplar ejemplarExistente=new Ejemplar();
        if (ejemplarExistenteOptional.isPresent()){
            ejemplarExistente=ejemplarExistenteOptional.get();
        }
        return new Venta(
                ejemplarExistente,
                LocalDateTime.now(ZoneId.systemDefault()),
                ejemplarExistente.getLibro().getPrecio()
        );
    }

    @Override
    public Venta obtenerVenta(String codLibro, Integer nroEjemplar) {
        return ventaRepository.obtenerVenta(codLibro, nroEjemplar);
    }
}
