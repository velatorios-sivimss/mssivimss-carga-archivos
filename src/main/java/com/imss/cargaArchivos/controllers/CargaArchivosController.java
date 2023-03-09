package com.imss.cargaArchivos.controllers;

import com.imss.cargaArchivos.models.response.CargaArchivosResponse;
import com.imss.cargaArchivos.models.response.DescargaArchivosResponse;
import com.imss.cargaArchivos.services.CargaArchivosService;
import com.imss.cargaArchivos.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("archivos")
public class CargaArchivosController {
    @Autowired
    CargaArchivosService carga;

    @PostMapping("cargaArchivo")
    public Response cargaArchivo(@RequestParam MultipartFile[] archivos, @RequestParam String datos) throws IOException {
        CargaArchivosResponse cr = carga.cargarArchivo(archivos, datos);
        if(cr.getEstatusCarga() == "archivos cargados"){
            return new Response(false,HttpStatus.OK.value(),"Se realizo la carga de los archivos" , cr);
        }
        return new Response(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Hubo un error" , cr);
    }

    @PostMapping("actualizaArchivo")
    public Response actualizarArchivo(@RequestParam MultipartFile[] archivos, @RequestParam String datos) throws IOException {
        CargaArchivosResponse cr = carga.actualizarArchivo(archivos, datos);
        if(cr.getEstatusCarga() == "archivos Actualizados"){
            return new Response(false,HttpStatus.OK.value(),"Se realizo la actualización de los archivos" , cr);
        }
        return new Response(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Hubo un error" , cr);
    }

    @GetMapping("descargarArchivo")
    public Response descargarArchivo(@RequestParam String datos) throws IOException {
        DescargaArchivosResponse dr = carga.descargarArchivo(datos);
        if(!dr.getArchivos().contains(null)){
            return new Response(false,HttpStatus.OK.value(),"Archivos:" , dr);
        }
        dr.setArchivos(new ArrayList<>());
        return new Response(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Hubo un error" , dr);
    }

}
