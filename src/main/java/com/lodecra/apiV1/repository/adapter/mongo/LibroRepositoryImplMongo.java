package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import com.lodecra.apiV1.repository.port.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.*;

@Repository
@Primary
public class LibroRepositoryImplMongo implements LibroRepository {
    private final LibroMongoRepository mongoRepository;
    private final LibroMapper mapper;

    private final MongoTemplate mongoTemplate;

    public LibroRepositoryImplMongo(LibroMongoRepository mongoRepository, LibroMapper mapper, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<Libro> obtenerTodosLosLibros(){
        var todosLosLibrosMongo = mongoRepository.findAll();
        return convertirListaLibrosMongoALibros(todosLosLibrosMongo);
    }

    @Override
    public List<Libro> obtenerLibrosPorBusquedaGeneral(String keyword) {
        var todosLosLibrosMongo = mongoRepository.findAll();
        var librosBuscados = todosLosLibrosMongo.stream().filter(libro->
                (null!=libro.autor()&&libro.autor().contains(keyword))
            ||(null!=libro.titulo()&&libro.titulo().contains(keyword))
            ||(null!=libro.editorial()&&libro.editorial().contains(keyword))
            ||(null!=libro.contacto()&&libro.contacto().contains(keyword))
        ).toList();
        return convertirListaLibrosMongoALibros(librosBuscados);
    }

    @Override
    public List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        Query query = new Query();
        query.addCriteria(Criteria.where(campoABuscar).is(keyword));
        var librosBuscados = mongoTemplate.find(query, LibroMongo.class);
        return convertirListaLibrosMongoALibros(librosBuscados);
    }

    private List<Libro> convertirListaLibrosMongoALibros(List<LibroMongo> original){
        List<Libro> aDevolver = new ArrayList<>();
        original.forEach(libroMongo -> aDevolver.add(mapper.libroMongoToLibro(libroMongo)));
        return aDevolver;
    }
}
