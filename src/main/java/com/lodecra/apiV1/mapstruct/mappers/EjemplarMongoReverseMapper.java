package com.lodecra.apiV1.mapstruct.mappers;

import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.repository.adapter.document.EjemplarMongo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EjemplarMongoReverseMapper extends Converter<EjemplarMongo,Ejemplar> {

    @Mapping(target = "libro.codigo", source = "codLibro")
    @Override
    Ejemplar convert(EjemplarMongo ejemplarMongo);

}
