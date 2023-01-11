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

    @Mapping(target = "book.code", ignore = true)
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
    @Mapping(target = "added", expression = "java(java.time.LocalDateTime.parse(ejemplar.getAgregado().toString(), java.time.format.DateTimeFormatter.ISO_DATE_TIME).format(java.time.format.DateTimeFormatter.ofPattern(\"dd-MM-yy HH:mm\")))")
    EjemplarDto ejemplarAndLibroToEjemplarDto(Ejemplar ejemplar, Libro libro);

    @Mapping(target = "libro.codigo", source = "codLibro")
    Ejemplar ejemplarMongoToEjemplar(EjemplarMongo ejemplarMongo);

    @Mapping(target = "vendidoFecha", ignore = true)
    @Mapping(target = "vendido", ignore = true)
    @Mapping(target = "precioVendido", ignore = true)
    @Mapping(source = "libro.codigo", target = "codLibro")
    EjemplarMongo ejemplarToEjemplarMongo(Ejemplar ejemplar);

}
