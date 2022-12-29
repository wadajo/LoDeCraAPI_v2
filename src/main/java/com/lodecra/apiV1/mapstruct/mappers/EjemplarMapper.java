package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EjemplarMapper {

    @Mapping(target = "book.code", source = "libro.codigo")
    @Mapping(target = "book.name", source = "libro.titulo")
    @Mapping(target = "book.author", source = "libro.autor")
    @Mapping(target = "book.price", source = "libro.precio", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "book.publisher", source = "libro.editorial")
    @Mapping(target = "book.contact", source = "libro.contacto")
    @Mapping(target = "book.stock", source = "libro.stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "book.discarded", source = "libro.descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "volumeNum", source = "nroEjemplar")
    @Mapping(target = "location", source = "ubicacion")
    @Mapping(target = "modality", source = "modalidad")
    @Mapping(target = "added", source = "agregado")
    EjemplarDto ejemplarToEjemplarDto(Ejemplar ejemplar);

    @Mapping(target = "libro.codigo", source = "codLibro")
    Ejemplar ejemplarMongoToEjemplar(EjemplarMongo ejemplarMongo);

}
