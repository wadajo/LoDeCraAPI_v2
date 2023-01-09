package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.exception.NoExistencesFoundException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    EjemplarRepository ejemplarRepository;

    LibroRepository libroRepository;

    public EjemplarServiceImpl(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<List<Ejemplar>> getEjemplaresPorCodigoLibro(String codLibro) throws WrongIdFormatException, BookNotFoundException {
        if (codLibro.length()!=8) {
            throw new WrongIdFormatException(codLibro);
        } else {
            var encontrados= ejemplarRepository.obtenerEjemplaresPorCodigoDeLibro(codLibro);
            if (encontrados.isPresent() && !encontrados.get().isEmpty())
                return encontrados;
            else
                throw new NoExistencesFoundException(codLibro);
        }
    }

    @Transactional
    @Override
    public Ejemplar guardarNuevoEjemplar(String codLibro, String ubicacion, String modalidad) throws BookNotSavedException {
        if (codLibro.length()!=8) {
            throw new WrongIdFormatException(codLibro);
        }
        Ejemplar construido=new Ejemplar();
        var libro=libroRepository.obtenerLibroPorCodigo(codLibro);
        AtomicInteger cantEjemplares = new AtomicInteger (ejemplarRepository
                .obtenerEjemplaresPorCodigoDeLibro(codLibro)
                .orElseGet(ArrayList<Ejemplar>::new)
                .size());
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
