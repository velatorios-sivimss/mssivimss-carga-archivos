package com.imss.cargaArchivos.models.response;

import lombok.Data;

import java.util.List;

@Data
public class CargaArchivosResponse {
private String estatusCarga = "error";
private List<String> rutaArchivos ;
}
