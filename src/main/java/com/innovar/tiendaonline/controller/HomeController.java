package com.innovar.tiendaonline.controller;

import com.innovar.tiendaonline.model.Producto;
import com.innovar.tiendaonline.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final ProductoRepository productoRepository;

    public HomeController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "categoria", required = false) String categoria, Model model) {
        if (categoria != null && !categoria.isEmpty()) {
            model.addAttribute("productos", productoRepository.findAll().stream()
                    .filter(p -> categoria.equalsIgnoreCase(p.getCategoria()))
                    .toList());
        } else {
            model.addAttribute("productos", productoRepository.findAll());
        }
        return "index";
    }
    // Vista de detalle del producto
    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + id));
        model.addAttribute("producto", producto);
        return "detalle-producto";
    }
}