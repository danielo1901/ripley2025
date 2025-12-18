package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import proyecto.com.pe.service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaViewController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Registro de Ventas");
        model.addAttribute("ventas", ventaService.listarVentas());
        return "ventas/lista";
    }
}
