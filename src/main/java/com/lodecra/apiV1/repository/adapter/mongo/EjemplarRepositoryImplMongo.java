package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class EjemplarRepositoryImplMongo implements EjemplarRepository {
    private final EjemplarMongoRepository mongoRepository;
    private final EjemplarMapper mapper;

    public EjemplarRepositoryImplMongo(EjemplarMongoRepository mongoRepository, EjemplarMapper mapper, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<List<Ejemplar>> obtenerEjemplaresPorCodigoDeLibro(String codLibro) {
        var todosLosEjemplares=mongoRepository.findAll();
        var ejemplaresEsteLibro=todosLosEjemplares.stream().filter(ej->ej.codLibro().equalsIgnoreCase(codLibro)).toList();
        List<Ejemplar> listaEjemplar=new ArrayList<>();
        ejemplaresEsteLibro.forEach(ejemplarMongo -> listaEjemplar.add(mapper.ejemplarMongoToEjemplar(ejemplarMongo)));
        return Optional.of(listaEjemplar);
    }
}
