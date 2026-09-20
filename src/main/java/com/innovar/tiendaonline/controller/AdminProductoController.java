package com.innovar.tiendaonline.controller;

import com.innovar.tiendaonline.model.Producto;
import com.innovar.tiendaonline.repository.ProductoRepository;
import com.innovar.tiendaonline.service.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductoRepository productoRepository;
    private final FileUploadService fileUploadService;

    public AdminProductoController(ProductoRepository productoRepository, FileUploadService fileUploadService) {
        this.productoRepository = productoRepository;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "admin/lista-productos";
    }

    // ASEGÚRATE DE QUE ESTE MÉTODOS TENGA 'model.addAttribute("producto", new Producto())'
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto()); // <-- ESTA LÍNEA ES VITAL
        return "admin/formulario-producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,@RequestParam(value = "file", required = false) MultipartFile archivo) throws IOException {
    
        // Si se subió un nuevo archivo, guardamos la nueva imagen
        if (archivo != null && !archivo.isEmpty()) {
            String rutaImagen = fileUploadService.guardarImagen(archivo);
            producto.setImagenUrl(rutaImagen);
        } else if (producto.getId() != null) {
            // Si es una edición y no se subió foto nueva, mantenemos la foto que ya tenía
            Producto productoExistente = productoRepository.findById(producto.getId()).orElse(null);
            if (productoExistente != null) {
                producto.setImagenUrl(productoExistente.getImagenUrl());
            }
    }

    productoRepository.save(producto);
    return "redirect:/admin/productos";
}

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("producto", producto);
        return "admin/formulario-producto";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "redirect:/admin/productos";
    }
}