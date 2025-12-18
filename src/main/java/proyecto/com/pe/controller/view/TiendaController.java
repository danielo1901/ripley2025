package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import proyecto.com.pe.service.ClienteService;
import proyecto.com.pe.service.EmpleadoService;
import proyecto.com.pe.service.ProductoService;
import proyecto.com.pe.service.VentaService;
import proyecto.com.pe.entity.Venta;
import proyecto.com.pe.entity.DetalleVenta;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String catalogo(Model model) {
        model.addAttribute("title", "Tienda Online - Ripley 2025");
        model.addAttribute("productos", productoService.listarProductos());
        return "tienda/catalogo";
    }

    @PostMapping("/comprar/{id}")
    public String comprar(@PathVariable Integer id, Authentication authentication) {
        try {
            var producto = productoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            var username = authentication.getName();
            var cliente = clienteService.buscarPorUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Obtener el primer empleado activo para procesar la venta (ej. 'admin')
            var empleado = empleadoService.buscarActivos().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay empleados activos para procesar la venta"));

            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setEmpleado(empleado);
            venta.setFecha(LocalDateTime.now());

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(1);
            detalle.setPrecioUnitario(producto.getPrecio());

            ventaService.registrarVentaCompleta(venta, List.of(detalle));

            return "redirect:/tienda?success";
        } catch (Exception e) {
            return "redirect:/tienda?error=" + e.getMessage();
        }
    }

    @GetMapping("/mis-compras")
    public String misCompras(Authentication authentication, Model model) {
        var username = authentication.getName();
        var cliente = clienteService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        model.addAttribute("title", "Mis Compras");
        model.addAttribute("ventas", ventaService.buscarPorCliente(cliente.getIdCliente()));
        return "tienda/mis-compras";
    }
}
