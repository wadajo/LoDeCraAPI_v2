package com.lodecra.apiV1.repository.adapter.document;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "libroscollection")
@CompoundIndex(def = "{'autor':1, 'titulo':1, 'editorial':1, 'contacto':1}", name = "general")
public record LibroMongo (@Field String codigo,
                          @Field String titulo,
                          @Field String autor,
                          @Field Integer precio,
                          @Field String editorial,
                          @Field String contacto,
                          @Field Integer stock,
                          @Field Boolean descartado) {}
