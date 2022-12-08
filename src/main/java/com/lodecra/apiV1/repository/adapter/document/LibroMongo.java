package com.lodecra.apiV1.repository.adapter.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(value = "libroscollection")
public class LibroMongo {

    @Id
    private String id;
    @Field
    private String titulo, autor;
    @Field
    private Integer precio;
    @Field
    private String editorial, contacto;
    @Field
    private Integer stock;
    @Field
    private Boolean descartado;
}
