package com.imss.cargaArchivos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CargarArchivoRequest {
    @JsonProperty
    private DatosCargaModel[] archivos;
}
