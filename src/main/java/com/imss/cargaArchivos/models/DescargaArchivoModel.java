package com.imss.cargaArchivos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DescargaArchivoModel {
    @JsonProperty
    private String ubicacion;
    @JsonProperty
    private String nombreArchivo;
}
