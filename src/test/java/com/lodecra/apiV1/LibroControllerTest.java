package com.lodecra.apiV1;

import com.lodecra.apiV1.controller.LibroController;
import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.entity.Libro;
import com.lodecra.apiV1.mapper.LibroMapper;
import com.lodecra.apiV1.service.LibroService;
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

        Libro libro = new Libro();
        libro.setTitulo("nuevo");

        List<Libro> todosLosLibros = List.of(libro);

        given(libroService.getLibros()).willReturn(todosLosLibros);
        given(mapper.libroADto(libro)).willReturn(new LibroDto(libro.getId(), libro.getTitulo()));

        mvc.perform(get("/libros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(libro.getTitulo())));
    }

    @Test
    public void cuandoLlamoAGetUnLibro_devuelveSoloEseLibro()
            throws Exception {

        Libro libro = new Libro();
        libro.setTitulo("unico");

        given(libroService.getLibroPorId(anyString())).willReturn(libro);
        given(mapper.libroADto(libro)).willReturn(new LibroDto(libro.getId(), libro.getTitulo()));

        mvc.perform(get("/libros/{id}","uno")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(libro.getTitulo())));
    }

    @Test
    public void cuandoBuscoUnLibroGral_devuelveListaConDosLibros()
            throws Exception {

        Libro libro1 = new Libro();
        libro1.setTitulo("El Señor de los Anillos I");
        Libro libro2 = new Libro();
        libro2.setTitulo("El Señor de los Anillos II");

        List<Libro> encontrados = List.of(libro1,libro2);

        given(libroService.getLibrosPorBusquedaGral(anyString())).willReturn(encontrados);
        given(mapper.libroADto(libro1)).willReturn(new LibroDto(libro1.getId(), libro1.getTitulo()));
        given(mapper.libroADto(libro2)).willReturn(new LibroDto(libro2.getId(), libro2.getTitulo()));

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

        Libro libro1 = new Libro();
        libro1.setTitulo("El Señor de los Anillos I");
        Libro libro2 = new Libro();
        libro2.setTitulo("El Señor de los Anillos II");

        List<Libro> encontrados = List.of(libro1,libro2);

        given(libroService.getLibrosPorBusquedaAvz(anyString(),anyString())).willReturn(encontrados);
        given(mapper.libroADto(libro1)).willReturn(new LibroDto(libro1.getId(), libro1.getTitulo()));
        given(mapper.libroADto(libro2)).willReturn(new LibroDto(libro2.getId(), libro2.getTitulo()));

        mvc.perform(get("/libros")
                        .queryParam("keyword","anillos")
                        .queryParam("campoABuscar","titulo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(libro1.getTitulo())));
    }
}
