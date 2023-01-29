package com.lodecra.apiV1.repository.adapter.mongo;

import com.lodecra.apiV1.exception.BookNotSavedException;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.repository.adapter.document.LibroMongo;
import com.lodecra.apiV1.repository.port.LibroRepository;
import com.lodecra.apiV1.util.Utilidades;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Transactional(readOnly = true)
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
    public Optional<List<Libro>> obtenerTodosLosLibrosDisponibles(){
        Query query=new Query();
        query.addCriteria(where("descartado").ne(true));
        var todosLosLibrosMongo=mongoTemplate.query(LibroMongo.class).matching(query).all();
        return conContenidoOVacio(todosLosLibrosMongo);
    }

    @Override
    public List<Libro> obtenerLibrosDisponiblesPorBusquedaGeneral(String keyword) {
        var todosLosLibros=obtenerTodosLosLibrosDisponibles();
        List<Libro> librosEncontrados = Collections.emptyList();
        if (todosLosLibros.isPresent())
            librosEncontrados= filtrarLibrosPorKeywordGeneral(keyword,todosLosLibros.get());
        return librosEncontrados;
    }

    @Override
    public List<Libro> obtenerLibrosPorBusquedaAvz(String keyword, String campoABuscar) {
        var todosLosLibros=obtenerTodosLosLibrosDisponibles();
        List<Libro> librosEncontrados = Collections.emptyList();
        if (todosLosLibros.isPresent())
            librosEncontrados = filtrarLibrosPorKeywordAvz(keyword,campoABuscar,todosLosLibros.get());
        return librosEncontrados;
    }

    @Override
    public Optional<Libro> obtenerLibroPorCodigo(String codigo) {
        var aDevolver = mongoRepository.findByCodigo(codigo);
        return aDevolver.map(mapper::libroMongoToLibro);
    }

    @Transactional
    @Override
    public Optional<Libro> crearNuevoLibro(Libro nuevo) {
        var posibleAGuardar=mapper.libroToLibroMongo(nuevo);
        String codigoDefault=posibleAGuardar.codigo();
        int prefix= Integer.parseInt(codigoDefault.substring(0,2));
        while (existeLibroConMismoCodigo(codigoDefault)){
            codigoDefault= Utilidades.construirCodigo(++prefix,posibleAGuardar.titulo(),posibleAGuardar.autor());
            Libro cambiado=mapper.libroMongoToLibro(posibleAGuardar);
            cambiado.setCodigo(codigoDefault);
            posibleAGuardar=new LibroMongo(cambiado.getCodigo(), cambiado.getTitulo(), cambiado.getAutor(), cambiado.getPrecio(), cambiado.getEditorial(), cambiado.getContacto(), cambiado.getStock(), cambiado.getDescartado());
        }
        LibroMongo creado = mongoRepository.save(posibleAGuardar);
        return Optional.of(mapper.libroMongoToLibro(mongoRepository.findByCodigo(creado.codigo()).orElseThrow(()-> new BookNotSavedException(nuevo.getTitulo()))));
    }

    @Override
    public Optional<Libro> buscarLibroPorTituloYAutor(String titulo, String autor) {
        var todosLosLibrosMongo = mongoRepository.findAll();
        var posible = todosLosLibrosMongo.stream().filter(libro ->
                (null != libro.autor() && libro.autor().equalsIgnoreCase(autor)) && (null != libro.titulo() && libro.titulo().equalsIgnoreCase(titulo))
        ).findAny();
        if(posible.isPresent()) {
            var encontrado = posible.get();
            return Optional.of(mapper.libroMongoToLibro(encontrado));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<Libro> editarLibroExistente(Libro editadoSinCodigo, String codLibro) {
        Optional<Libro> aDevolver = Optional.empty();
        if(mongoRepository.findByCodigo(codLibro).isPresent()) {
            LibroMongo nuevo = construirLibroMongoEditado(editadoSinCodigo, codLibro);
            mongoTemplate
                    .update(LibroMongo.class)
                    .matching(query(where("codigo").is(codLibro)))
                    .replaceWith(nuevo)
                    .findAndReplace();
            aDevolver = Optional.of(mapper.libroMongoToLibro(nuevo));
        }
        return aDevolver;
    }

    @Transactional
    @Override
    public void descartarLibro(String codLibro) {
        if(mongoRepository.findByCodigo(codLibro).isPresent()) {
            Update update = new Update();
            update.set("descartado",Boolean.TRUE);
            mongoTemplate
                    .update(LibroMongo.class)
                    .matching(query(where("codigo").is(codLibro)))
                    .apply(update)
                    .first();
        }
    }

    private LibroMongo construirLibroMongoEditado(Libro editadoSinCodigo, String codigo) {
        return new LibroMongo(codigo,
                null!= editadoSinCodigo.getTitulo() ? editadoSinCodigo.getTitulo() : mongoRepository.findByCodigo(codigo).orElseThrow().titulo(),
                null!= editadoSinCodigo.getAutor() ? editadoSinCodigo.getAutor() : mongoRepository.findByCodigo(codigo).orElseThrow().autor(),
                null!= editadoSinCodigo.getPrecio() ? editadoSinCodigo.getPrecio() : mongoRepository.findByCodigo(codigo).orElseThrow().precio(),
                null!= editadoSinCodigo.getEditorial() ? editadoSinCodigo.getEditorial() : mongoRepository.findByCodigo(codigo).orElseThrow().editorial(),
                null!= editadoSinCodigo.getContacto() ? editadoSinCodigo.getContacto() : mongoRepository.findByCodigo(codigo).orElseThrow().contacto(),
                null!= editadoSinCodigo.getStock() ? editadoSinCodigo.getStock() : mongoRepository.findByCodigo(codigo).orElseThrow().stock(),
                null!= editadoSinCodigo.getDescartado() ? editadoSinCodigo.getDescartado() : mongoRepository.findByCodigo(codigo).orElseThrow().descartado());
    }

    private List<Libro> convertirListaLibrosMongoALibros(List<LibroMongo> original){
        List<Libro> aDevolver = new ArrayList<>();
        original.forEach(libroMongo -> aDevolver.add(mapper.libroMongoToLibro(libroMongo)));
        return aDevolver;
    }

    private List<Libro> filtrarLibrosPorKeywordGeneral(String keyword, List<Libro> libros) {
        String keywordLC=keyword.toLowerCase();
        return libros.stream().filter(libro ->
                (null != libro.getAutor() && libro.getAutor().toLowerCase().contains(keywordLC))
                        || (null != libro.getTitulo() && libro.getTitulo().toLowerCase().contains(keywordLC))
                        || (null != libro.getEditorial() && libro.getEditorial().toLowerCase().contains(keywordLC))
                        || (null != libro.getContacto() && libro.getContacto().toLowerCase().contains(keywordLC))
        ).toList();
    }

    private List<Libro> filtrarLibrosPorKeywordAvz(String keyword, String campoABuscar, List<Libro> todosLosLibros) {
        String keywordLC=keyword.toLowerCase();
        return todosLosLibros.stream().filter(libro -> switch (campoABuscar){
            case "autor":
                if(null != libro.getAutor() && libro.getAutor().toLowerCase().contains(keywordLC))
                    yield true;
            case "titulo":
                if(null != libro.getTitulo() && libro.getTitulo().toLowerCase().contains(keywordLC))
                    yield true;
            case "editorial":
                if(null != libro.getEditorial() && libro.getEditorial().toLowerCase().contains(keywordLC))
                    yield true;
            case "contacto":
                if(null != libro.getContacto() && libro.getContacto().toLowerCase().contains(keywordLC))
                    yield true;
            default:
                yield false;
        }).toList();
    }

    private Optional<List<Libro>> conContenidoOVacio(List<LibroMongo> librosMongo) {
        return librosMongo.isEmpty() ?
                Optional.empty() :
                Optional.of(convertirListaLibrosMongoALibros(librosMongo));
    }

    private boolean existeLibroConMismoCodigo (String codigo){
        Optional<LibroMongo> existenteEnLaBase=mongoRepository.findByCodigo(codigo);
        return existenteEnLaBase.filter(libroMongo -> codigo.equals(libroMongo.codigo())).isPresent();
    }
}
