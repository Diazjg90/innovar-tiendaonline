package com.innovar.tiendaonline.controller;

import com.innovar.tiendaonline.model.ItemCarrito;
import com.innovar.tiendaonline.model.Producto;
import com.innovar.tiendaonline.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final ProductoRepository productoRepository;

    public CarritoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> obtenerCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    // Ver el carrito
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Construcción del mensaje predeterminado para WhatsApp
        StringBuilder mensaje = new StringBuilder("¡Hola! Quisiera realizar el siguiente pedido:\n\n");
        for (ItemCarrito item : carrito) {
            mensaje.append("• ").append(item.getProducto().getTitulo())
                   .append(" (x").append(item.getCantidad()).append(")")
                   .append(" - $").append(item.getSubtotal()).append("\n");
        }
        mensaje.append("\nTotal estimado: $").append(total)
               .append("\n\nQuedo a la espera para enviarle los detalles del diseño personalizado.");

        String mensajeUrl = URLEncoder.encode(mensaje.toString(), StandardCharsets.UTF_8);

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        model.addAttribute("mensajeWhatsapp", mensajeUrl);

        return "carrito";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, 
                                   @RequestParam(value = "cantidad", defaultValue = "1") int cantidad, 
                                   HttpSession session) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            List<ItemCarrito> carrito = obtenerCarrito(session);
            boolean existe = false;

            for (ItemCarrito item : carrito) {
                if (item.getProducto().getId().equals(id)) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                carrito.add(new ItemCarrito(producto, cantidad));
            }
        }
        return "redirect:/carrito";
    }

    // Eliminar un producto del carrito
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> item.getProducto().getId().equals(id));
        return "redirect:/carrito";
    }

    // Actualizar la cantidad de un producto específico en el carrito
    @PostMapping("/actualizar/{id}")
    public String actualizarCantidad(@PathVariable Long id, 
                                    @RequestParam("cantidad") int cantidad, 
                                    HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        
        if (cantidad > 0) {
            for (ItemCarrito item : carrito) {
                if (item.getProducto().getId().equals(id)) {
                    item.setCantidad(cantidad);
                    break;
                }
            }
        } else {
            // Si se coloca cantidad 0 o menos, se remueve el producto
            carrito.removeIf(item -> item.getProducto().getId().equals(id));
        }
        
        return "redirect:/carrito";
    }
}