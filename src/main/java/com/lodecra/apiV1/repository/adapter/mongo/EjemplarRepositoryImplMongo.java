package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
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

    private final ConversionService conversionService;

    private final MongoTemplate mongoTemplate;

    public EjemplarRepositoryImplMongo(EjemplarMongoRepository mongoRepository, LibroRepository libroRepository, ConversionService conversionService, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.libroRepository = libroRepository;
        this.conversionService = conversionService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Ejemplar> obtenerEjemplaresNoVendidosPorCodigo(String codLibro) {
        var todosLosEjemplares=mongoRepository.findAllByCodLibro(codLibro);
        List<Ejemplar> ejemplaresNoVendidos=new ArrayList<>();
        todosLosEjemplares.ifPresent(ejemplaresMongo -> ejemplaresMongo.forEach(
                ejemplarMongo -> {
                    if(null==ejemplarMongo.vendidoFecha())
                        ejemplaresNoVendidos.add(conversionService.convert(ejemplarMongo,Ejemplar.class));
                }));

        return ejemplaresNoVendidos;
    }

    @Override
    public Integer obtenerCantidadDeEjemplaresTotalesPorCodigo(String codLibro) {
        Query query=new Query();
        query.addCriteria(where("codLibro").is(codLibro));
        var cantEjemplares=mongoTemplate.count(query,EjemplarMongo.class);
        return Integer.parseInt(String.valueOf(cantEjemplares));
    }

    @Override
    public Optional<Ejemplar> obtenerEjemplarNro(String codLibro, Integer nroEjemplar) {
        var ejemplarADevolver=mongoRepository.findByCodLibroAndNroEjemplar(codLibro, nroEjemplar);
        if (ejemplarADevolver.isPresent()) {
            var libroAAnadir=libroRepository.obtenerLibroPorCodigo(codLibro);
            if (libroAAnadir.isPresent()) {
                Ejemplar encontrado = conversionService.convert(ejemplarADevolver.get(),Ejemplar.class);
                if (encontrado != null) {
                    encontrado.setLibro(libroAAnadir.get());
                    return Optional.of(encontrado);
                }
            }
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void agregarEjemplar(Ejemplar nuevo) {
        var agregado=mongoRepository.save(conversionService.convert(nuevo,EjemplarMongo.class));
        var encontrado=mongoRepository.findByCodLibroAndNroEjemplar(agregado.codLibro(),agregado.nroEjemplar());
        conversionService.convert(encontrado.orElseThrow(()->new BookNotSavedException(nuevo.getLibro().getTitulo())),Ejemplar.class);
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
