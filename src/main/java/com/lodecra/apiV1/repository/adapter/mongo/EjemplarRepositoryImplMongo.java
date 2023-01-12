package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
@Primary
public class EjemplarRepositoryImplMongo implements EjemplarRepository {
    private final EjemplarMongoRepository mongoRepository;
    private final EjemplarMapper mapper;

    public EjemplarRepositoryImplMongo(EjemplarMongoRepository mongoRepository, EjemplarMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<List<Ejemplar>> obtenerEjemplaresPorCodigoDeLibro(String codLibro) {
        var todosLosEjemplares=mongoRepository.findAllByCodLibro(codLibro);
        List<Ejemplar> listaEjemplar=new ArrayList<>();
        todosLosEjemplares.ifPresent(ejemplaresMongo -> ejemplaresMongo.forEach(
                ejemplarMongo -> listaEjemplar.add(mapper.ejemplarMongoToEjemplar(ejemplarMongo))));
        return listaEjemplar.isEmpty() ?
                Optional.empty() :
                Optional.of(listaEjemplar);
    }

    @Override
    public Optional<Ejemplar> obtenerEjemplarNro(String codLibro, Integer nroEjemplar) {
        Ejemplar encontrado=null;
        var ejemplarADevolver=mongoRepository.findByCodLibroAndNroEjemplar(codLibro, nroEjemplar);
        if (ejemplarADevolver.isPresent())
            encontrado=mapper.ejemplarMongoToEjemplar(ejemplarADevolver.get());
        return Optional.ofNullable(encontrado);
    }

    @Transactional
    @Override
    public Ejemplar agregarEjemplar(Ejemplar nuevo) {
        var agregado=mongoRepository.save(mapper.ejemplarToEjemplarMongo(nuevo));
        var encontrado=mongoRepository.findByCodLibroAndNroEjemplar(agregado.codLibro(),agregado.nroEjemplar());
        return mapper.ejemplarMongoToEjemplar(encontrado.orElseThrow(()->new BookNotSavedException(nuevo.getLibro().getTitulo())));
    }
}
