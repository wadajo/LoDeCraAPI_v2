package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
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

    private final EjemplarMongoRepository ejemplarMongoRepository;

    private final VentaMongoRepository ventaMongoRepository;

    private final VentaMapper mapper;

    private final MongoTemplate mongoTemplate;

    public VentaRepositoryImplMongo(EjemplarMongoRepository ejemplarMongoRepository, VentaMongoRepository ventaMongoRepository, VentaMapper mapper, MongoTemplate mongoTemplate) {
        this.ejemplarMongoRepository = ejemplarMongoRepository;
        this.ventaMongoRepository = ventaMongoRepository;
        this.mapper = mapper;
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
        var ventas=mongoTemplate.query(EjemplarMongo.class).matching(query).all();

        ventas.forEach(venta->ventasDeLibro.add(mapper.ejemplarMongoToVenta(venta)));

        return ventasDeLibro;
    }


}
