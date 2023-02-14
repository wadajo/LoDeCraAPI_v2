package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.VentaRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class VentaServiceImpl implements VentaService {

    private final EjemplarService ejemplarService;

    private final VentaRepository ventaRepository;

    private final EjemplarRepository ejemplarRepository;

    public VentaServiceImpl(EjemplarService ejemplarService, VentaRepository ventaRepository, EjemplarRepository ejemplarRepository) {
        this.ejemplarService = ejemplarService;
        this.ventaRepository = ventaRepository;
        this.ejemplarRepository = ejemplarRepository;
    }


    @Override
    public Venta hacerVentaRapida(String codLibro, Integer nroEjemplar) throws VolumeAlreadySoldException {
        Venta aHacer = crearVentaAhora(codLibro, nroEjemplar);
        ejemplarRepository.venderEjemplar(aHacer);
        return aHacer;
    }

    @Override
    public List<Venta> listarVentasDelLibro(String codLibro) {
        return ventaRepository.todasLasVentasDelLibro(codLibro);
    }

    private Venta crearVentaAhora(String codLibro, Integer nroEjemplar) throws VolumeAlreadySoldException {
        if (ventaRepository.estaVendido(codLibro, nroEjemplar))
            throw new VolumeAlreadySoldException(codLibro,nroEjemplar);

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


}
