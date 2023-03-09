package com.imss.cargaArchivos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DatosActualizaModel {
    @JsonProperty
    private String ubicacionActual;
    @JsonProperty
    private String nombreArchivoActual;
    private String nombreNuevo;
    @JsonProperty
    private int indice;
}
