package com.lodecra.apiV1.repository.adapter.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(value = "ejemplarescollection")
public record EjemplarMongo(
        @Field String codLibro,
        @Field Integer nroEjemplar,
        @Field String ubicacion,
        @Field String modalidad,
        @Field LocalDateTime agregado,
        @Field LocalDateTime vendidoFecha,
        @Field Integer precioVendido,
        @Field Boolean vendido
) {}
