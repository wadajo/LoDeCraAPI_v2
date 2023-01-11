package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.port.VentaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
@Primary
public class VentaRepositoryImplMongo implements VentaRepository {

    VentaMongoRepository repository;

    VentaMapper mapper;

    public VentaRepositoryImplMongo(VentaMongoRepository repository, VentaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public void saveVenta(Venta venta) {
        var ventaMongo=mapper.ventaToEjemplarMongo(venta);
        repository.save(ventaMongo);
    }
}
