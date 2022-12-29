package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Libro;
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
    @Mapping(target = "book.publisher", source = "libro.editorial", defaultValue = "N/A")
    @Mapping(target = "book.contact", source = "libro.contacto", defaultValue = "N/A")
    @Mapping(target = "book.stock", source = "libro.stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "book.discarded", source = "libro.descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "volumeNum", source = "ejemplar.nroEjemplar")
    @Mapping(target = "location", source = "ejemplar.ubicacion")
    @Mapping(target = "modality", source = "ejemplar.modalidad")
    @Mapping(target = "added", source = "ejemplar.agregado")
    EjemplarDto ejemplarAndLibroToEjemplarDto(Ejemplar ejemplar, Libro libro);

    @Mapping(target = "libro.codigo", source = "codLibro")
    Ejemplar ejemplarMongoToEjemplar(EjemplarMongo ejemplarMongo);

}
