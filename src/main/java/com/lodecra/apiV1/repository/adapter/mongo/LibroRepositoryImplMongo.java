package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.port.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class LibroRepositoryImplMongo implements LibroRepository {
    private final LibroMongoRepository mongoRepository;
    private final LibroMapper mapper;

    public LibroRepositoryImplMongo(LibroMongoRepository mongoRepository, LibroMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }
    @Override
    public List<Libro> obtenerTodosLosLibros(){
        var todosLosLibrosMongo = mongoRepository.findAll();
        List<Libro> aDevolver = new ArrayList<>();
        todosLosLibrosMongo.forEach(libroMongo -> aDevolver.add(mapper.libroMongoToLibro(libroMongo)));
        return aDevolver;
    }
}
