package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VentaMongoRepository extends MongoRepository<EjemplarMongo,String> {
}
