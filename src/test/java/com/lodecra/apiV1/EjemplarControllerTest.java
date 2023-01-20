package com.lodecra.apiV1;

import com.lodecra.apiV1.controller.EjemplarController;
import com.lodecra.apiV1.dto.EjemplarDto;
import com.lodecra.apiV1.dto.LibroDto;
import com.lodecra.apiV1.mapstruct.mappers.EjemplarMapper;
import com.lodecra.apiV1.model.Ejemplar;
import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.EjemplarService;
import com.lodecra.apiV1.service.port.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EjemplarController.class)
public class EjemplarControllerTest {

    @MockBean
    private EjemplarService ejemplarService;
    @MockBean
    private LibroService libroService;
    @MockBean
    private EjemplarMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Value("${lodecra.baseUrl}")
    private String baseUrl;

    @Test
    void cuandoLlamoAGetEjemplaresDeUnLibro_devuelveSuListaDeEjemplares()
            throws Exception {

        Libro libro = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);
        Ejemplar ejemplar=new Ejemplar(libro,1,"Madrid","firme", LocalDateTime.now());

        given(libroService.getLibroPorCodigo(anyString())).willReturn(Optional.of(libro));
        given(ejemplarService.getEjemplaresPorCodigoLibro(anyString())).willReturn(Optional.of(List.of(ejemplar)));
        given(mapper.ejemplarAndLibroToEjemplarDto(any(Ejemplar.class),any(Libro.class))).willReturn(new EjemplarDto(new LibroDto("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false),1,"Madrid","firme",LocalDateTime.now().toString()));

        mvc.perform(get(baseUrl+"/ejemplares/{codLibro}","55_EmaAi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].book.name", is(libro.getTitulo())));
    }

    @Test
    void cuandoAgregoUnEjemplar_agregaYDevuelveEnElBody() throws Exception{
        Libro libro1 = new Libro("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false);
        Ejemplar ejemplar1=new Ejemplar(libro1,1,"Madrid","firme", LocalDateTime.now());

        given(ejemplarService.guardarNuevoEjemplar(anyString(),anyString(),anyString())).willReturn(ejemplar1);
        given(mapper.ejemplarAndLibroToEjemplarDto(any(Ejemplar.class),any(Libro.class))).willReturn(new EjemplarDto(new LibroDto("55_EmaAi","Ema, la cautiva","Aira",990,"EUDEBA",null,1,false),1,"Madrid","firme",LocalDateTime.now().toString()));

        mvc.perform(post(baseUrl+"/ejemplares/{codLibro}","55_EmaAi")
                        .queryParam("ubicacion","Madrid")
                        .queryParam("modalidad","firme"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.name", is(libro1.getTitulo())));

    }

}
