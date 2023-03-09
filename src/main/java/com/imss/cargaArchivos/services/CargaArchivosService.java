package com.imss.cargaArchivos.services;

import com.imss.cargaArchivos.models.response.CargaArchivosResponse;
import com.imss.cargaArchivos.models.response.DescargaArchivosResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CargaArchivosService {
    CargaArchivosResponse cargarArchivo(MultipartFile[] archivos, String datos) throws IOException;

    CargaArchivosResponse actualizarArchivo(MultipartFile[] archivos, String datos) throws IOException;

    DescargaArchivosResponse descargarArchivo(String datos) throws IOException;
}
