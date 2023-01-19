package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Transactional(readOnly = true)
@Repository
@Primary
public class EjemplarRepositoryImplMongo implements EjemplarRepository {
    private final EjemplarMongoRepository mongoRepository;

    private final LibroRepository libroRepository;

    private final EjemplarMapper mapper;

    private final MongoTemplate mongoTemplate;

    public EjemplarRepositoryImplMongo(EjemplarMongoRepository mongoRepository, LibroRepository libroRepository, EjemplarMapper mapper, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.libroRepository = libroRepository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
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
        if (ejemplarADevolver.isPresent()) {
            var libroAAnadir=libroRepository.obtenerLibroPorCodigo(codLibro);
            if (libroAAnadir.isPresent()) {
                encontrado = mapper.ejemplarMongoToEjemplar(ejemplarADevolver.get());
                encontrado.setLibro(libroAAnadir.get());
            }
        }
        return Optional.ofNullable(encontrado);
    }

    @Transactional
    @Override
    public Ejemplar agregarEjemplar(Ejemplar nuevo) {
        var agregado=mongoRepository.save(mapper.ejemplarToEjemplarMongo(nuevo));
        var encontrado=mongoRepository.findByCodLibroAndNroEjemplar(agregado.codLibro(),agregado.nroEjemplar());
        return mapper.ejemplarMongoToEjemplar(encontrado.orElseThrow(()->new BookNotSavedException(nuevo.getLibro().getTitulo())));
    }

    @Transactional
    @Override
    public void venderEjemplar(Venta ventaAHacer) {
        Query query=new Query();
        query.addCriteria(where("nroEjemplar").is(ventaAHacer.getEjemplarVendido().getNroEjemplar()));
        query.addCriteria(where("codLibro").is(ventaAHacer.getEjemplarVendido().getLibro().getCodigo()));
        Update update = new Update();
        update.set("vendidoFecha",ventaAHacer.getFechaHoraVendido());
        update.set("precioVendido",ventaAHacer.getPrecioVendido());
        mongoTemplate.updateFirst(query,update,EjemplarMongo.class);
    }
}
