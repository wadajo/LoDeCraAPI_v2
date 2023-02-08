package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EjemplarMongoMapper extends Converter<Ejemplar,EjemplarMongo> {

    @Mapping(target = "vendidoFecha", ignore = true)
    @Mapping(target = "precioVendido", ignore = true)
    @Mapping(source = "libro.codigo", target = "codLibro")
    @Override
    EjemplarMongo convert(Ejemplar ejemplar);

}
