package com.lodecra.apiV1;

import com.lodecra.apiV1.controller.LibroController;
import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.mapstruct.mappers.LibroMapper;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void cuandoLlamoAGetLibros_devuelveListaConUnSoloLibro()
            throws Exception {

        Libro libro = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);

        List<Libro> todosLosLibros = List.of(libro);

        given(libroService.getLibros()).willReturn(todosLosLibros);
        given(mapper.libroToLibroDto(libro)).willReturn(new LibroDto(libro.getId(), libro.getTitulo(),libro.getAutor(),libro.getPrecio(), libro.getEditorial(), libro.getContacto(), libro.getStock(), libro.getDescartado()));

        mvc.perform(get("/libros")
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

        given(libroService.getLibroPorId(anyString())).willReturn(libro);
        given(mapper.libroToLibroDto(libro)).willReturn(new LibroDto(libro.getId(), libro.getTitulo(),libro.getAutor(),libro.getPrecio(), libro.getEditorial(), libro.getContacto(), libro.getStock(), libro.getDescartado()));

        mvc.perform(get("/libros/{id}","uno")
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

        given(libroService.getLibrosPorBusquedaGral(anyString())).willReturn(encontrados);
        given(mapper.libroToLibroDto(libro1)).willReturn(new LibroDto(libro1.getId(), libro1.getTitulo(),libro1.getAutor(),libro1.getPrecio(), libro1.getEditorial(), libro1.getContacto(), libro1.getStock(), libro1.getDescartado()));
        given(mapper.libroToLibroDto(libro2)).willReturn(new LibroDto(libro2.getId(), libro2.getTitulo(),libro2.getAutor(),libro2.getPrecio(), libro2.getEditorial(), libro2.getContacto(), libro2.getStock(), libro2.getDescartado()));

        mvc.perform(get("/libros")
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

        given(libroService.getLibrosPorBusquedaAvz(anyString(),anyString())).willReturn(encontrados);
        given(mapper.libroToLibroDto(libro1)).willReturn(new LibroDto(libro1.getId(), libro1.getTitulo(),libro1.getAutor(),libro1.getPrecio(), libro1.getEditorial(), libro1.getContacto(), libro1.getStock(), libro1.getDescartado()));
        given(mapper.libroToLibroDto(libro2)).willReturn(new LibroDto(libro2.getId(), libro2.getTitulo(),libro2.getAutor(),libro2.getPrecio(), libro2.getEditorial(), libro2.getContacto(), libro2.getStock(), libro2.getDescartado()));

        mvc.perform(get("/libros")
                        .queryParam("keyword","anillos")
                        .queryParam("campoABuscar","titulo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(libro1.getTitulo())));
    }
}
