package com.lodecra.apiV1.util;

import org.springframework.stereotype.Component;

@Component
public class Utilidades {

    public static String construirCodigo (int prefix, String titulo, String autor){
        String codigo=prefix+"_";
        String[] datos= emprolijarTildesYMayusculas(autor, titulo);
        autor=datos[0];
        titulo=datos[1];
        if ((autor.length()>=2) && (titulo.length()>=3)){
            codigo += titulo.substring(0,3)+autor.substring(0,2);
        } else if (autor.length()>=2) {
            codigo += titulo+"_"+autor.substring(0,2);
        } else if (titulo.length()>=3) {
            codigo += titulo.substring(0,3)+autor+"_";
        } else {
            codigo += titulo+"_"+autor+"_";
        }
        return codigo;
    }

    private static String[] emprolijarTildesYMayusculas (String autor, String titulo) {
        String autorNuevo= autor.trim().replaceAll("\\s+","");
        String tituloNuevo= titulo.trim().replaceAll("\\s+","");
        String[] nuevos=new String[2];

        autorNuevo=autorNuevo.contains("ñ")?autorNuevo.replace("ñ", "n"):autorNuevo;
        autorNuevo=autorNuevo.contains("Ñ")?autorNuevo.replace("Ñ", "N"):autorNuevo;
        autorNuevo=autorNuevo.contains("Á")?autorNuevo.replace("Á", "A"):autorNuevo;
        autorNuevo=autorNuevo.contains("É")?autorNuevo.replace("É", "E"):autorNuevo;
        autorNuevo=autorNuevo.contains("Í")?autorNuevo.replace("Í", "I"):autorNuevo;
        autorNuevo=autorNuevo.contains("Ó")?autorNuevo.replace("Ó", "O"):autorNuevo;
        autorNuevo=autorNuevo.contains("Ú")?autorNuevo.replace("Ú", "U"):autorNuevo;
        autorNuevo=autorNuevo.contains("á")?autorNuevo.replace("á", "a"):autorNuevo;
        autorNuevo=autorNuevo.contains("é")?autorNuevo.replace("é", "e"):autorNuevo;
        autorNuevo=autorNuevo.contains("í")?autorNuevo.replace("í", "i"):autorNuevo;
        autorNuevo=autorNuevo.contains("ó")?autorNuevo.replace("ó", "o"):autorNuevo;
        autorNuevo=autorNuevo.contains("ú")?autorNuevo.replace("ú", "u"):autorNuevo;
        tituloNuevo=tituloNuevo.contains("¿")?tituloNuevo.replace("¿", "_"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("ñ")?tituloNuevo.replace("ñ", "n"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("Ñ")?tituloNuevo.replace("Ñ", "N"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("¡")?tituloNuevo.replace("¡", "_"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("á")?tituloNuevo.replace("á", "a"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("é")?tituloNuevo.replace("é", "e"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("í")?tituloNuevo.replace("í", "i"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("ó")?tituloNuevo.replace("ó", "o"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("ú")?tituloNuevo.replace("ú", "u"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("Á")?tituloNuevo.replace("Á", "A"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("É")?tituloNuevo.replace("É", "E"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("Í")?tituloNuevo.replace("Í", "I"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("Ó")?tituloNuevo.replace("Ó", "O"):tituloNuevo;
        tituloNuevo=tituloNuevo.contains("Ú")?tituloNuevo.replace("Ú", "U"):tituloNuevo;

        nuevos[0]=autorNuevo;
        nuevos[1]=tituloNuevo;
        return nuevos;
    }
}
