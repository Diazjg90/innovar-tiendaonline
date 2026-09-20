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
    public String verDetalle(@PathVariable("id") Long id, Model model) {
        // Buscar en la base de datos de Aiven o redirigir si no existe
        Producto producto = productoRepository.findById(id)
                .orElse(null);

        if (producto == null) {
            return "redirect:/"; // Redirige al inicio si el ID no existe
        }

        model.addAttribute("producto", producto);
        return "detalle-producto"; // Nombre de tu plantilla HTML
    }
}