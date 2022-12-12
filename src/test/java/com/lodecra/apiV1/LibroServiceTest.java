package com.lodecra.apiV1;

import com.lodecra.apiV1.model.Libro;
import com.lodecra.apiV1.service.port.LibroService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroService libroService;
    private Libro libro1;
    private Libro libro2;
    List<Libro> listaDeLibros;

    @BeforeEach
    void setUp() {
        listaDeLibros=new ArrayList<>();
        libro1=new Libro();
        libro2=new Libro();
        listaDeLibros.addAll(List.of(libro1,libro2));
    }

    @AfterEach
    void tearDown() {
        libro1=libro2=null;
        listaDeLibros=null;
    }

    @Test
    public void cuandoPreguntoSiTieneElMismoCodigo_respondeSi(){
        libro1.setCodigo("Uno");
        libro2.setCodigo("Uno");

        when(libroService.existeLibroConMismoCodigo(eq(libro1.getCodigo()))).thenReturn(true);

        Boolean resultado = libroService.existeLibroConMismoCodigo(libro2.getCodigo());
        assertEquals(resultado,true);
    }
}
