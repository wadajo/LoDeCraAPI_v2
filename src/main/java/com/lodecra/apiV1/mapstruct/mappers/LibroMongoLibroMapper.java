package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibroMongoLibroMapper extends Converter<LibroMongo,Libro> {

    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "descartado", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Override
    Libro convert(LibroMongo libroMongo);

}