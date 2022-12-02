package com.lodecra.apiV1.mapper;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public interface LibroMapper {

    LibroDto libroADto(Libro original);

}
