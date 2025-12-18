package proyecto.com.pe.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import proyecto.com.pe.service.ProductoService;
import proyecto.com.pe.service.ClienteService;
import proyecto.com.pe.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard - Ripley 2025");
        model.addAttribute("totalProductos", productoService.listarProductos().size());
        model.addAttribute("totalClientes", clienteService.listarClientes().size());
        model.addAttribute("totalVentas", ventaService.listarVentas().size());
        return "index";
    }
}
