package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.model.Libro;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibroBookDtoMapper extends Converter<Libro,BookDto> {

    @Mapping(target = "code", source = "codigo")
    @Mapping(target = "name", source = "titulo", defaultValue = "N/A")
    @Mapping(target = "author", source = "autor", defaultValue = "N/A")
    @Mapping(target = "price", source = "precio", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "publisher", source = "editorial", defaultValue = "N/A")
    @Mapping(target = "contact", source = "contacto", defaultValue = "N/A")
    @Mapping(target = "stock", source = "stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "discarded", source = "descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Override
    BookDto convert(Libro libro);

}