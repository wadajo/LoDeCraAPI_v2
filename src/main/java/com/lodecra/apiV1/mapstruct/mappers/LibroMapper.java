package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibroMapper {

    @Mapping(target = "code", source = "codigo")
    @Mapping(target = "name", source = "titulo", defaultValue = "N/A")
    @Mapping(target = "author", source = "autor", defaultValue = "N/A")
    @Mapping(target = "price", source = "precio", defaultValue = "0")
    @Mapping(target = "publisher", source = "editorial", defaultValue = "N/A")
    @Mapping(target = "contact", source = "contacto", defaultValue = "N/A")
    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "discarded", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    LibroDto libroToLibroDto(Libro libro);

    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "descartado", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Libro libroMongoToLibro(LibroMongo libroMongo);

}