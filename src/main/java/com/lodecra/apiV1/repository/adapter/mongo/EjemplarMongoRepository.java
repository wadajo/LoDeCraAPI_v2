package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EjemplarMongoRepository extends MongoRepository<EjemplarMongo,String> {

    Optional<EjemplarMongo> findByCodLibroAndNroEjemplar(String codLibro, Integer nroEjemplar);

}
