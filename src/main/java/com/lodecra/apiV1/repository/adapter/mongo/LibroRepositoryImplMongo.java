package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import com.lodecra.apiV1.repository.port.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return convertirListaLibrosMongoALibros(todosLosLibrosMongo);
    }

    @Override
    public List<Libro> obtenerLibrosPorBusquedaGeneral(String keyword) {
        var librosEncontrados = filtrarLibrosPorKeywordGeneral(keyword, mongoRepository.findAll());
        return convertirListaLibrosMongoALibros(librosEncontrados);
    }

    @Override
    public List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        var librosEncontrados = filtrarLibrosPorKeywordAvz(keyword,campoABuscar,mongoRepository.findAll());
        return convertirListaLibrosMongoALibros(librosEncontrados);
    }

    @Override
    public Optional<Libro> obtenerLibroPorCodigo(String codigo) {
        var aDevolver = mongoRepository.findByCodigo(codigo);
        return aDevolver.map(mapper::libroMongoToLibro);
    }

    private List<Libro> convertirListaLibrosMongoALibros(List<LibroMongo> original){
        List<Libro> aDevolver = new ArrayList<>();
        original.forEach(libroMongo -> aDevolver.add(mapper.libroMongoToLibro(libroMongo)));
        return aDevolver;
    }

    private List<LibroMongo> filtrarLibrosPorKeywordGeneral(String keyword, List<LibroMongo> todosLosLibrosMongo) {
        String keywordLC=keyword.toLowerCase();
        return todosLosLibrosMongo.stream().filter(libro ->
                (null != libro.autor() && libro.autor().toLowerCase().contains(keywordLC))
                        || (null != libro.titulo() && libro.titulo().toLowerCase().contains(keywordLC))
                        || (null != libro.editorial() && libro.editorial().toLowerCase().contains(keywordLC))
                        || (null != libro.contacto() && libro.contacto().toLowerCase().contains(keywordLC))
        ).toList();
    }

    private List<LibroMongo> filtrarLibrosPorKeywordAvz(String keyword, String campoABuscar, List<LibroMongo> todosLosLibrosMongo) {
        String keywordLC=keyword.toLowerCase();
        return todosLosLibrosMongo.stream().filter(libro -> switch (campoABuscar){
            case "autor":
                if(null != libro.autor() && libro.autor().toLowerCase().contains(keywordLC))
                    yield true;
            case "titulo":
                if(null != libro.titulo() && libro.titulo().toLowerCase().contains(keywordLC))
                    yield true;
            case "editorial":
                if(null != libro.editorial() && libro.editorial().toLowerCase().contains(keywordLC))
                    yield true;
            case "contacto":
                if(null != libro.contacto() && libro.contacto().toLowerCase().contains(keywordLC))
                    yield true;
            default:
                yield false;
        }).toList();
    }
}
