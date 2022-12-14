package com.lodecra.apiV1.repository.adapter.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "libroscollection")
public record LibroMongo (@Field String codigo,
                          @Field String titulo,
                          @Field String autor,
                          @Field Integer precio,
                          @Field String editorial,
                          @Field String contacto,
                          @Field Integer stock,
                          @Field Boolean descartado) {}
