package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.Proveedor;
import proyecto.com.pe.service.ProveedorService;

@Controller
@RequestMapping("/proveedores")
public class ProveedorViewController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Lista de Proveedores");
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("title", "Nuevo Proveedor");
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        model.addAttribute("title", "Editar Proveedor");
        model.addAttribute("proveedor", proveedor);
        return "proveedores/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveedor proveedor) {
        if (proveedor.getIdProveedor() != null) {
            proveedorService.actualizar(proveedor);
        } else {
            proveedorService.registrar(proveedor);
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }
}
