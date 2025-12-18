package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Lista de Clientes");
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("title", "Nuevo Cliente");
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("title", "Editar Cliente");
        model.addAttribute("cliente", cliente);
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        if (cliente.getIdCliente() != null) {
            clienteService.actualizar(cliente);
        } else {
            clienteService.registrar(cliente);
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
