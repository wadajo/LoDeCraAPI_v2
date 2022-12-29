package com.lodecra.apiV1.service;

import com.lodecra.apiV1.exception.BookNotFoundException;
import com.lodecra.apiV1.exception.WrongIdFormatException;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.service.port.EjemplarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    EjemplarRepository repository;

    public EjemplarServiceImpl(EjemplarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<List<Ejemplar>> getEjemplaresPorCodigoLibro(String codLibro) throws WrongIdFormatException, BookNotFoundException {
        if (codLibro.length()!=8) {
            throw new WrongIdFormatException(codLibro);
        } else {
            var encontrados=repository.obtenerEjemplaresPorCodigoDeLibro(codLibro);
            if (encontrados.isPresent() && !encontrados.get().isEmpty())
                return encontrados;
            else
                throw new BookNotFoundException(codLibro);
        }
    }
}
