package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import com.lodecra.apiV1.repository.port.EjemplarRepository;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.repository.port.VentaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Transactional(readOnly = true)
@Repository
@Primary
public class VentaRepositoryImplMongo implements VentaRepository {

    private final EjemplarRepository ejemplarRepository;

    private final EjemplarMongoRepository ejemplarMongoRepository;

    private final LibroRepository libroRepository;

    private final MongoTemplate mongoTemplate;

    public VentaRepositoryImplMongo(EjemplarMongoRepository ejemplarMongoRepository, EjemplarRepository ejemplarRepository, LibroRepository libroRepository, MongoTemplate mongoTemplate) {
        this.ejemplarRepository = ejemplarRepository;
        this.ejemplarMongoRepository = ejemplarMongoRepository;
        this.libroRepository = libroRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean estaVendido(String codLibro, Integer nroEjemplar) {
        var ejemplarVendidoOptional= ejemplarMongoRepository.findByCodLibroAndNroEjemplar(codLibro, nroEjemplar);
        return ejemplarVendidoOptional
                .filter(ejemplarMongo -> null != ejemplarMongo.vendidoFecha())
                .isPresent();
    }

    @Override
    public List<Venta> todasLasVentasDelLibro(String codLibro) {
        List<Venta> ventasDeLibro=new ArrayList<>();
        Query query=new Query();
        query.addCriteria(where("vendidoFecha").ne(null));
        query.addCriteria(where("codLibro").is(codLibro));
        var ventasEjemplarMongo=mongoTemplate.query(EjemplarMongo.class).matching(query).all();

        ventasEjemplarMongo.forEach(ejMongo-> {
            var ejADevolver=ejemplarRepository.obtenerEjemplarNro(ejMongo.codLibro(),ejMongo.nroEjemplar());
            if (ejADevolver.isPresent()) {
                var ejADevolverCompleto=ejADevolver.get();
                var libroADevolver=libroRepository.obtenerLibroPorCodigo(ejMongo.codLibro());
                if (libroADevolver.isPresent()) {
                    ejADevolverCompleto.setLibro(libroADevolver.get());
                    Venta ventaADevolver = new Venta(ejADevolverCompleto, ejMongo.vendidoFecha(), ejMongo.precioVendido());
                    ventasDeLibro.add(ventaADevolver);
                }
            }
            }
        );

        return ventasDeLibro;
    }


}
