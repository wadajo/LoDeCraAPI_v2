package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.*;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional(readOnly = true)
@Service
@Observed(name = "service-ejemplares")
public class EjemplarServiceImpl implements EjemplarService {

    EjemplarRepository ejemplarRepository;

    LibroRepository libroRepository;

    public EjemplarServiceImpl(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Observed(name = "ejemplares-disponibles-por-codigo")
    public List<Ejemplar> getEjemplaresDisponiblesPorCodigo(String codLibro) throws BookNotFoundException {
        var encontrados= ejemplarRepository.obtenerEjemplaresNoVendidosPorCodigo(codLibro);
        if (!encontrados.isEmpty())
            return encontrados;
        else
            throw new NoExistencesFoundException(codLibro);
    }

    @Override
    @Observed(name = "ejemplar-nro-por-codigo")
    public Optional<Ejemplar> getEjemplarNro(String codLibro, Integer nroEjemplar) throws WrongVolumeNoException {
        var ejemplarEncontrado=ejemplarRepository.obtenerEjemplarNro(codLibro, nroEjemplar);
        if (ejemplarEncontrado.isPresent())
            return ejemplarEncontrado;
        else
            throw new WrongVolumeNoException(codLibro,nroEjemplar);
    }

    @Transactional
    @Override
    @Observed(name = "nuevo-ejemplar")
    public Ejemplar guardarNuevoEjemplar(String codLibro, String ubicacion, String modalidad) throws BookNotSavedException {
        Ejemplar construido=new Ejemplar();
        var libro=libroRepository.obtenerLibroPorCodigo(codLibro);
        AtomicInteger cantEjemplares = new AtomicInteger (ejemplarRepository
                .obtenerCantidadDeEjemplaresTotalesPorCodigo(codLibro));
        if (libro.isPresent()) {
            construido.setLibro(libro.get());
            construido.setModalidad(modalidad);
            construido.setUbicacion(ubicacion);
            construido.setNroEjemplar(cantEjemplares.incrementAndGet());
            construido.setAgregado(LocalDateTime.now(ZoneId.systemDefault()));
            ejemplarRepository.agregarEjemplar(construido);
        } else {
            throw new BookNotFoundException(codLibro);
        }
        return construido;
    }

}
