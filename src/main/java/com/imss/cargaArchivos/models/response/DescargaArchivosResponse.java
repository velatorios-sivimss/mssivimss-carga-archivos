package com.imss.cargaArchivos.models.response;

import com.imss.cargaArchivos.models.DatosArchivoModel;
import lombok.Data;

import java.util.List;

@Data
public class DescargaArchivosResponse {
    List<DatosArchivoModel> archivos;
}
