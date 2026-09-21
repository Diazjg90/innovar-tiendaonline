package com.innovar.tiendaonline.controller;

import com.innovar.tiendaonline.model.Producto;
import com.innovar.tiendaonline.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        List<Producto> productos = productoRepository.findAll();
        // Filtra la lista para eliminar cualquier elemento nulo
        if (productos != null) {
            productos.removeIf(Objects::isNull);
        }
        model.addAttribute("productos", productos != null ? productos : new ArrayList<>());
        return "index";
    }

    @GetMapping("/producto/{id}")
    public String verDetalle(@PathVariable("id") Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/";
        }
        model.addAttribute("producto", producto);
        return "detalle-producto";
    }
}