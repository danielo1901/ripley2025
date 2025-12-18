package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.Categoria;
import proyecto.com.pe.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaViewController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Lista de Categorías");
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("title", "Nueva Categoría");
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        model.addAttribute("title", "Editar Categoría");
        model.addAttribute("categoria", categoria);
        return "categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        if (categoria.getIdCategoria() != null) {
            categoriaService.actualizar(categoria.getIdCategoria(), categoria);
        } else {
            categoriaService.registrar(categoria);
        }
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return "redirect:/categorias";
    }
}
