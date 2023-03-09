package com.imss.cargaArchivos.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ValidacionesArchivosUtil {

    public static String obtenerFormatoArchivo(MultipartFile archivo){
        String nombreArchivo = archivo.getOriginalFilename();
        if(StringUtils.endsWithIgnoreCase(nombreArchivo, "pdf")){
            return ".pdf";
        } else if (StringUtils.endsWithIgnoreCase(nombreArchivo, "png")) {
            return ".png";
        } else if (StringUtils.endsWithIgnoreCase(nombreArchivo, "jpg")) {
            return ".jpg";
        }else if (StringUtils.endsWithIgnoreCase(nombreArchivo, "jpeg")) {
            return ".jpeg";
        }
        return "Formatyo no permitido";
    }

    public static Boolean validarArchivosPermitidos(String fileName) {
        if (StringUtils.endsWithIgnoreCase(fileName, "png") || StringUtils.endsWithIgnoreCase(fileName, "pdf") || StringUtils.endsWithIgnoreCase(fileName, "jpg") || StringUtils.endsWithIgnoreCase(fileName, "jpeg")) {
            return true;
        }
        return false;
    }

    public static Boolean validarCarpeta(String ubicacion) {
        File file = new File(ubicacion);
        return file.isDirectory();
    }

    public static Boolean validarArchivo(String ubicacion, String nombreArchivo) {
        File file = new File(ubicacion + "/" + nombreArchivo + ".pdf");
        return file.exists();
    }
}
