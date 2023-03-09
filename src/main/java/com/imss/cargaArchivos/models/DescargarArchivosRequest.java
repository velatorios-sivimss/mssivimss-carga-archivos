package com.imss.cargaArchivos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DescargarArchivosRequest {
    @JsonProperty
    private DescargaArchivoModel[]archivos;
}
