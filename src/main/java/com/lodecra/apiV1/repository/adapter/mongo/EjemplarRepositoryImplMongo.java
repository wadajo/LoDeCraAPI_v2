package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
