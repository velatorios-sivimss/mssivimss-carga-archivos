package com.imss.cargaArchivos.services.impl;

import com.google.gson.Gson;
import com.imss.cargaArchivos.models.*;
import com.imss.cargaArchivos.models.response.CargaArchivosResponse;
import com.imss.cargaArchivos.models.response.DescargaArchivosResponse;
import com.imss.cargaArchivos.services.CargaArchivosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.imss.cargaArchivos.util.ValidacionesArchivosUtil.*;

@Service
@Slf4j
public class CargaArchivoServiceImpl implements CargaArchivosService {
    Gson json = new Gson();
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    String separador = IS_UNIX ? "/" : "\\";

    @Override
    public CargaArchivosResponse cargarArchivo(MultipartFile[] archivos, String datos) throws IOException {
        CargaArchivosResponse cr = new CargaArchivosResponse();
        List<String> rutasGuardadasList = new ArrayList<>();
        List<MultipartFile> listaArchivosPermitidos = new ArrayList<>();
        CargarArchivoRequest conv = json.fromJson(datos, CargarArchivoRequest.class);
        int indice = 1;
        if (archivos.length == conv.getArchivos().length) {
            for (DatosCargaModel dataFile : conv.getArchivos()) {
                if (validarCarpeta(dataFile.getUbicacion())) {
                    if (validarArchivosPermitidos(archivos[dataFile.getIndice()].getOriginalFilename())) {
                        listaArchivosPermitidos.add(archivos[dataFile.getIndice()]);
                    } else {
                        cr.setEstatusCarga("Hubo un error, no todos los archivos son pdf o imagenes");
                        cr.setRutaArchivos(new ArrayList<>());
                        return cr;
                    }
                } else {
                    cr.setEstatusCarga("No existe la carpeta " + dataFile.getUbicacion());
                    cr.setRutaArchivos(new ArrayList<>());
                    return cr;
                }
            }
            if (listaArchivosPermitidos.size() == archivos.length) {
                for (DatosCargaModel dataFile : conv.getArchivos()) {
                    rutasGuardadasList.add(escribirArchivo(archivos[dataFile.getIndice()], dataFile.getUbicacion(), "ArchivoNo" + indice++));
                }
                cr.setEstatusCarga("archivos cargados");
                cr.setRutaArchivos(rutasGuardadasList);
                return cr;
            }

        }
        cr.setEstatusCarga("Hubo un error");
        cr.setRutaArchivos(new ArrayList<>());
        return cr;
    }

    public String escribirArchivo(MultipartFile archivo, String ubicacion, String nombreArchivo) throws IOException {

        if (validarCarpeta(ubicacion)) {
            File creaArchivo = new File(ubicacion + "/" + nombreArchivo + obtenerFormatoArchivo(archivo));
            archivo.transferTo(creaArchivo);
            return ubicacion + "/" + nombreArchivo + obtenerFormatoArchivo(archivo);
        }
        return "no existe la carpeta";
    }

    @Override
    public CargaArchivosResponse actualizarArchivo(MultipartFile[] archivos, String datos) throws IOException {
        ActualizaArchivosRequest conv = json.fromJson(datos, ActualizaArchivosRequest.class);
        CargaArchivosResponse cr = new CargaArchivosResponse();
        List<String> rutasActualizadasList = new ArrayList<>();
        List<MultipartFile> archivosActualizablesList = new ArrayList<>();
        if (archivos.length == conv.getArchivos().length) {
            for (DatosActualizaModel dataFile : conv.getArchivos()) {
                if (validarCarpeta(dataFile.getUbicacionActual())) {
                    archivosActualizablesList.add(archivos[dataFile.getIndice()]);
                } else {
                    cr.setEstatusCarga("No existe la carpeta");
                    cr.setRutaArchivos(new ArrayList<>());
                    return cr;
                }
            }
            if(archivosActualizablesList.size() == archivos.length){
                for(DatosActualizaModel dataFile : conv.getArchivos()){
                    rutasActualizadasList.add(actualiza(archivos[dataFile.getIndice()], dataFile.getUbicacionActual(), dataFile.getNombreArchivoActual(), dataFile.getNombreNuevo()));
                }

                cr.setEstatusCarga("archivos Actualizados");
                cr.setRutaArchivos(rutasActualizadasList);
                return cr;
            }

        }
        cr.setEstatusCarga("Error en la cantidad de matriz de archivos y datos");
        cr.setRutaArchivos(new ArrayList<>());
        return cr;
    }
    public String actualiza(MultipartFile archivo, String ubicacion, String nombreArchivo, String nombreNuevo) throws IOException {
        String nombreArchivoOriginal = archivo.getOriginalFilename();
        if (validarCarpeta(ubicacion)) {
            if (validarArchivosPermitidos(nombreArchivoOriginal)) {
                File archivoAnterior = new File(ubicacion + "/" + nombreArchivo + obtenerFormatoArchivo(archivo));
                archivoAnterior.delete();
                File creaArchivo = new File(ubicacion + "/" + nombreNuevo + obtenerFormatoArchivo(archivo));
                archivo.transferTo(creaArchivo);
                return ubicacion + "/" + nombreNuevo + obtenerFormatoArchivo(archivo);
            }
            return "error, el archivo no es permitido";
        }
        return "no existe la carpeta";
    }

    @Override
    public DescargaArchivosResponse descargarArchivo(String datos) throws IOException {
        DescargaArchivosResponse dr = new DescargaArchivosResponse();
        List<byte[]> listaArchivoByte = new ArrayList<>();
        List<DatosArchivoModel> listaDatos = new ArrayList<>();
        DescargarArchivosRequest conv = json.fromJson(datos, DescargarArchivosRequest.class);
        //byte[] fileContent = new byte[0];
        for (DescargaArchivoModel dataFile : conv.getArchivos()) {
            //listaArchivoByte.add(obtenerArchivo(dataFile.getUbicacion(), dataFile.getNombreArchivo()));
            listaDatos.add(obtenerArchivo(dataFile.getUbicacion(), dataFile.getNombreArchivo()));
        }
        dr.setArchivos(listaDatos);
        return dr;
    }

    public DatosArchivoModel obtenerArchivo(String ubicacion, String nombre) throws IOException {
       DatosArchivoModel dtm = new DatosArchivoModel();
        try{
           File file = new File(ubicacion + "/" + nombre);
           if(Objects.isNull(file)){
               dtm.setNombreArchivo(nombre);
               dtm.setCadena(new byte[0]);
               return dtm;
           }
           byte[] fileContent = Files.readAllBytes(file.toPath());
           dtm.setNombreArchivo(nombre);
           dtm.setCadena(fileContent);
           return dtm;
       }catch (Exception ex){
            dtm.setNombreArchivo(nombre);
            dtm.setCadena(new byte[0]);
            return dtm;
       }

    }

//    public DescargaArchivosResponse descargarArreglo(String ubicacion, String nombre){
//        File file = new File(ubicacion + "/" + nombre + ".pdf");
//    }



}
