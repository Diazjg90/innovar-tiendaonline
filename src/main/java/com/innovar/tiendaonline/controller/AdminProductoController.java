package com.innovar.tiendaonline.controller;

import com.innovar.tiendaonline.model.Producto;
import com.innovar.tiendaonline.repository.ProductoRepository;
import com.innovar.tiendaonline.service.CloudinaryService;
import com.innovar.tiendaonline.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/admin/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, @RequestParam("file") MultipartFile file) throws IOException {
        
        if (!file.isEmpty()) {
            String urlImagen = cloudinaryService.subirImagen(file);
            producto.setImagenUrl(urlImagen);
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