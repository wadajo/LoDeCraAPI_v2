package com.lodecra.apiV1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lodecra.apiV1.controller.LibroController;
import com.lodecra.apiV1.dto.BookDto;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import com.lodecra.apiV1.util.Utilidades;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @MockBean
    private LibroService libroService;
    @MockBean
    private LibroMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Value("${lodecra.baseUrl}")
    private String baseUrl;

    @Test
    public void cuandoLlamoAGetLibros_devuelveListaConUnSoloLibro()
            throws Exception {

        Libro libro = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);

        List<Libro> todosLosLibros = List.of(libro);

        given(libroService.getLibrosDisponibles()).willReturn(Optional.of(todosLosLibros));
        given(mapper.libroToBookDto(libro)).willReturn(new BookDto(libro.getCodigo(), libro.getTitulo(),libro.getAutor(),libro.getPrecio(), libro.getEditorial(), libro.getContacto(), libro.getStock(), libro.getDescartado()));

        mvc.perform(get(baseUrl+"/libros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(libro.getTitulo())));
    }

    @Test
    public void cuandoLlamoAGetUnLibro_devuelveSoloEseLibro()
            throws Exception {

        Libro libro = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);
        libro.setTitulo("unico");

        given(libroService.getLibroPorCodigo(anyString())).willReturn(Optional.of(libro));
        given(mapper.libroToBookDto(libro)).willReturn(new BookDto(libro.getCodigo(), libro.getTitulo(),libro.getAutor(),libro.getPrecio(), libro.getEditorial(), libro.getContacto(), libro.getStock(), libro.getDescartado()));

        mvc.perform(get(baseUrl+"/libros/{id}","uno")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(libro.getTitulo())));
    }

    @Test
    public void cuandoBuscoUnLibroGral_devuelveListaConDosLibros()
            throws Exception {

        Libro libro1 = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);
        Libro libro2 = new Libro("55_FesAi","Festival","Aira",990,"Blatt&Ríos",null,1,false);

        List<Libro> encontrados = List.of(libro1,libro2);

        given(libroService.getLibrosPorBusquedaGral(anyString())).willReturn(Optional.of(encontrados));
        given(mapper.libroToBookDto(libro1)).willReturn(new BookDto(libro1.getCodigo(), libro1.getTitulo(),libro1.getAutor(),libro1.getPrecio(), libro1.getEditorial(), libro1.getContacto(), libro1.getStock(), libro1.getDescartado()));
        given(mapper.libroToBookDto(libro2)).willReturn(new BookDto(libro2.getCodigo(), libro2.getTitulo(),libro2.getAutor(),libro2.getPrecio(), libro2.getEditorial(), libro2.getContacto(), libro2.getStock(), libro2.getDescartado()));

        mvc.perform(get(baseUrl+"/libros")
                        .queryParam("keyword","anillos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(libro1.getTitulo())));
    }

    @Test
    public void cuandoBuscoUnLibroAvz_devuelveListaConDosLibros()
            throws Exception {

        Libro libro1 = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);
        Libro libro2 = new Libro("55_FesAi","Festival","Aira",990,"Blatt&Ríos",null,1,false);

        List<Libro> encontrados = List.of(libro1,libro2);

        given(libroService.getLibrosPorBusquedaAvz(anyString(),anyString())).willReturn(Optional.of(encontrados));
        given(mapper.libroToBookDto(libro1)).willReturn(new BookDto(libro1.getCodigo(), libro1.getTitulo(),libro1.getAutor(),libro1.getPrecio(), libro1.getEditorial(), libro1.getContacto(), libro1.getStock(), libro1.getDescartado()));
        given(mapper.libroToBookDto(libro2)).willReturn(new BookDto(libro2.getCodigo(), libro2.getTitulo(),libro2.getAutor(),libro2.getPrecio(), libro2.getEditorial(), libro2.getContacto(), libro2.getStock(), libro2.getDescartado()));

        mvc.perform(get(baseUrl+"/libros")
                        .queryParam("keyword","anillos")
                        .queryParam("campoABuscar","titulo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(libro1.getTitulo())));
    }

    @Test
    void cuandoGuardoUnLibroConDatosIncompletos_loGuardaYGeneraCorrectamenteElCodigo() throws Exception {
        Libro libro1 = new Libro();
        libro1.setTitulo("El divorcio");
        libro1.setAutor("Aira");
        libro1.setEditorial("Mansalva");

        String libro1Json = new ObjectMapper().writeValueAsString(libro1);

        given(libroService.guardarNuevoLibro(libro1)).willReturn(Optional.of(libro1));
        given(mapper.libroToBookDto(libro1)).willReturn(new BookDto(Utilidades.construirCodigo(55,libro1.getTitulo(),libro1.getAutor()), libro1.getTitulo(),libro1.getAutor(),libro1.getPrecio(), libro1.getEditorial(), libro1.getContacto(), libro1.getStock(), libro1.getDescartado()));

        mvc.perform(post(baseUrl+"/libros")
                        .content(libro1Json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(libro1.getTitulo())))
                .andExpect(jsonPath("$.code", is(Utilidades.construirCodigo(55,libro1.getTitulo(),libro1.getAutor()))));
    }
}
