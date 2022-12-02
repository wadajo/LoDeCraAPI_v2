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

        Libro libro = new Libro(null,"prueba");

        List<Libro> todosLosLibros = List.of(libro);

        given(libroService.getLibros()).willReturn(todosLosLibros);
        given(mapper.libroADto(libro)).willReturn(new LibroDto(libro.getId(), libro.getNombre()));

        mvc.perform(get("/libros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(libro.getNombre())));
    }
}
