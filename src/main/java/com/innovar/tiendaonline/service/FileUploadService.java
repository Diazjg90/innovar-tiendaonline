package com.innovar.tiendaonline.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileUploadService {

    private final String uploadDir = "uploads/";

    public String guardarImagen(MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) {
            return null;
        }

        // Crear el directorio 'uploads' si no existe
        Path pathDirectorio = Paths.get(uploadDir);
        if (!Files.exists(pathDirectorio)) {
            Files.createDirectories(pathDirectorio);
        }

        // Nombre de archivo único para evitar colisiones
        String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = pathDirectorio.resolve(nombreArchivo);

        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + nombreArchivo;
    }
}
