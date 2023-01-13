package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import com.lodecra.apiV1.repository.port.VentaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Transactional(readOnly = true)
@Repository
@Primary
public class VentaRepositoryImplMongo implements VentaRepository {

    private final EjemplarMongoRepository ejemplarRepository;

    private final VentaMapper mapper;

    private final MongoTemplate mongoTemplate;

    public VentaRepositoryImplMongo(EjemplarMongoRepository ejemplarRepository, VentaMapper mapper, MongoTemplate mongoTemplate) {
        this.ejemplarRepository = ejemplarRepository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    @Override
    public void saveVenta(Venta aGuardar) {
        var ejemplarMongoAGuardar=mapper.ventaToEjemplarMongo(aGuardar);
        mongoTemplate
                .update(EjemplarMongo.class)
                .matching(query(where("nroEjemplar").is(ejemplarMongoAGuardar.nroEjemplar())).addCriteria(where("codigo").is(ejemplarMongoAGuardar.codLibro())))
                .replaceWith(ejemplarMongoAGuardar)
                .findAndReplace();
//        mongoTemplate.save(ejemplarMongoAGuardar,"ejemplarescollection");
    }

    @Override
    public Venta obtenerVenta(String codLibro, Integer nroEjemplar) {
        var ejemplarVendido=ejemplarRepository.findByCodLibroAndNroEjemplar(codLibro, nroEjemplar);
        Venta aDevolver;
        if (ejemplarVendido.isPresent()){
            aDevolver=mapper.ejemplarMongoToVenta(ejemplarVendido.get());
        } else {
            aDevolver=new Venta();
        }
        return aDevolver;
    }


}
