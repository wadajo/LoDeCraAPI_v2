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
    @Mapping(target = "price", source = "precio", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "publisher", source = "editorial", defaultValue = "N/A")
    @Mapping(target = "contact", source = "contacto", defaultValue = "N/A")
    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "discarded", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    LibroDto libroToLibroDto(Libro libro);

    @Mapping(target = "codigo", source = "code")
    @Mapping(target = "titulo", source = "name", defaultValue = "N/A")
    @Mapping(target = "autor", source = "author", defaultValue = "N/A")
    @Mapping(target = "precio", source = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "editorial", source = "publisher", defaultValue = "N/A")
    @Mapping(target = "contacto", source = "contact", defaultValue = "N/A")
    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "descartado", source = "discarded", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Libro libroDtoToLibro(LibroDto libroDto);

    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "descartado", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Libro libroMongoToLibro(LibroMongo libroMongo);

    @Mapping(target = "codigo", expression = "java(com.lodecra.apiV1.util.Utilidades.construirCodigo(55,titulo,autor))")
    LibroMongo libroToLibroMongo(Libro libro);

}