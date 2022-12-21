package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LibroMongoRepository extends MongoRepository<LibroMongo,String> {
    Optional<LibroMongo> findByCodigo(String codigo);
}
