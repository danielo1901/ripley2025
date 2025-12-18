package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.service.EmpleadoService;

@Controller
@RequestMapping("/empleados")
public class EmpleadoViewController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Lista de Empleados");
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        return "empleados/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("title", "Nuevo Empleado");
        model.addAttribute("empleado", new Empleado());
        return "empleados/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Empleado empleado = empleadoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        model.addAttribute("title", "Editar Empleado");
        model.addAttribute("empleado", empleado);
        return "empleados/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Empleado empleado) {
        if (empleado.getIdEmpleado() != null) {
            empleadoService.actualizar(empleado);
        } else {
            empleadoService.registrar(empleado);
        }
        return "redirect:/empleados";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados";
    }
}
