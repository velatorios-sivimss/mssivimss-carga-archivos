package com.imss.cargaArchivos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DatosCargaModel {
    @JsonProperty
    private String ubicacion;
//    @JsonProperty
//    private String nombreArchivo;
    @JsonProperty
    private int indice;
}
