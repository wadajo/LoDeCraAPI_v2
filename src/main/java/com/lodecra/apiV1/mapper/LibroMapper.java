package com.lodecra.apiV1.mapper;

import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {

    public LibroDto libroADto(Libro original) {
        if (null!=original.getPrecio() && null!=original.getStock())
            return new LibroDto(original.getId(), original.getTitulo(), original.getAutor(), original.getPrecio(), original.getEditorial(), original.getContacto(), original.getStock(), original.getDescartado());
        else
            return new LibroDto(original.getId(), original.getTitulo(), original.getAutor(), 0, original.getEditorial(), original.getContacto(), 0, original.getDescartado());
    }
    public Libro libroMongoALibro(LibroMongo original) {
        return new Libro(original.getId(), original.getTitulo(), original.getAutor(),original.getPrecio(), original.getEditorial(), original.getContacto(), original.getStock(), original.getDescartado());
    }
}
