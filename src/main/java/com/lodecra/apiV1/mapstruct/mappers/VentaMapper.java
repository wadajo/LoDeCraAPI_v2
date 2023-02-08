package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.dto.VentaDto;
import com.lodecra.apiV1.model.Venta;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VentaMapper extends Converter<Venta,VentaDto> {

    @Mapping(target = "volume", source = "venta.ejemplarVendido")
    @Mapping(target = "volume.book.code", ignore = true)
    @Mapping(target = "volume.book.name", source = "venta.ejemplarVendido.libro.titulo")
    @Mapping(target = "volume.book.author", source = "venta.ejemplarVendido.libro.autor")
    @Mapping(target = "volume.book.price", source = "venta.ejemplarVendido.libro.precio", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "volume.book.publisher", source = "venta.ejemplarVendido.libro.editorial", defaultValue = "N/A")
    @Mapping(target = "volume.book.contact", source = "venta.ejemplarVendido.libro.contacto", defaultValue = "N/A")
    @Mapping(target = "volume.book.stock", source = "venta.ejemplarVendido.libro.stock", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "volume.book.discarded", source = "venta.ejemplarVendido.libro.descartado", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "volume.volumeNum", source = "venta.ejemplarVendido.nroEjemplar")
    @Mapping(target = "volume.location", source = "venta.ejemplarVendido.ubicacion")
    @Mapping(target = "volume.modality", source = "venta.ejemplarVendido.modalidad")
    @Mapping(target = "volume.added", source = "venta.ejemplarVendido.agregado")
    @Mapping(target = "dateSold", source = "venta.fechaHoraVendido")
    @Mapping(target = "priceSold", source = "venta.precioVendido")
    VentaDto convert(Venta venta);

}
