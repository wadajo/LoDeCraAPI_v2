package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibroLibroMongoMapper extends Converter<Libro,LibroMongo> {

    @Mapping(target = "codigo", expression = "java(com.lodecra.apiV1.util.Utilidades.construirCodigo(55,titulo,autor))")
    @Override
    LibroMongo convert(Libro libro);

}