package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LibroMongoRepository extends MongoRepository<LibroMongo,String> {
}
