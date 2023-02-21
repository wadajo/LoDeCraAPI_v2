package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotSoldException;
import com.lodecra.apiV1.exception.VolumeAlreadySoldException;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.VentaRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.VentaService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Transactional(readOnly = true)
@Service
@Observed(name = "service-ventas")
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
    @Observed(name = "nueva-venta")
    public Venta hacerVenta(String codLibro, Integer nroEjemplar, Integer precioVendido, LocalDateTime fechaHoraVendido) throws VolumeAlreadySoldException, BookNotSoldException{
        if (ventaRepository.estaVendido(codLibro, nroEjemplar)) {
            throw new VolumeAlreadySoldException(codLibro, nroEjemplar);
        }
        var ejemplarExistenteOptional=ejemplarService.getEjemplarNro(codLibro, nroEjemplar);
        if (ejemplarExistenteOptional.isPresent()){
            Ejemplar ejemplarExistente=ejemplarExistenteOptional.get();
            Venta aHacer = new Venta(
                    ejemplarExistente,
                    null!=fechaHoraVendido?fechaHoraVendido:LocalDateTime.now(ZoneId.systemDefault()),
                    null!=precioVendido?precioVendido:ejemplarExistente.getLibro().getPrecio()
            );
            ejemplarRepository.venderEjemplar(aHacer);
            return aHacer;
        } else {
            throw new BookNotSoldException(codLibro, nroEjemplar);
        }
    }

    @Override
    @Observed(name = "ventas-hechas-por-codigo")
    public List<Venta> listarVentasDelLibro(String codLibro) {
        return ventaRepository.todasLasVentasDelLibro(codLibro);
    }

}
