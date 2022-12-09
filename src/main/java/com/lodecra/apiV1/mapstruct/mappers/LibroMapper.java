package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibroMapper {

    @Mapping(target = "name", source = "titulo", defaultValue = "N/A")
    @Mapping(target = "author", source = "autor", defaultValue = "N/A")
    @Mapping(target = "price", source = "precio", defaultValue = "0")
    @Mapping(target = "publisher", source = "editorial", defaultValue = "N/A")
    @Mapping(target = "contact", source = "contacto", defaultValue = "N/A")
    @Mapping(target = "stock", source = "stock", defaultValue = "0")
    @Mapping(target = "discarded", source = "descartado", defaultValue = "false")
    LibroDto libroToLibroDto(Libro libro);

    Libro libroMongoToLibro(LibroMongo libroMongo);
}
