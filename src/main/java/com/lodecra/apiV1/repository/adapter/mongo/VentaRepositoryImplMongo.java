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

    private final VentaMongoRepository repository;

    private final VentaMapper mapper;

    private final MongoTemplate mongoTemplate;

    public VentaRepositoryImplMongo(VentaMongoRepository repository, VentaMapper mapper, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    @Override
    public void saveVenta(Venta venta) {
        var ejemplarAVender=crearEjemplarVendido(venta);
        mongoTemplate
                .update(EjemplarMongo.class)
                .matching(query(where("nroEjemplar").is(ejemplarAVender.nroEjemplar())).addCriteria(where("codigo").is(ejemplarAVender.codLibro())))
                .replaceWith(ejemplarAVender)
                .findAndReplace();
//        repository.save(ventaMongo);
    }

    private EjemplarMongo crearEjemplarVendido(Venta venta){
        return new EjemplarMongo(
                venta.getEjemplarVendido().getLibro().getCodigo(),
                venta.getEjemplarVendido().getNroEjemplar(),
                venta.getEjemplarVendido().getUbicacion(),
                venta.getEjemplarVendido().getModalidad(),
                venta.getEjemplarVendido().getAgregado(),
                venta.getFechaHoraVendido(),
                venta.getPrecioVendido(),
                Boolean.TRUE
        );
    }

}
