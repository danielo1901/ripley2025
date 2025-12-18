package proyecto.com.pe.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.Producto;
import proyecto.com.pe.service.ProductoService;
import proyecto.com.pe.service.CategoriaService;
import proyecto.com.pe.service.ProveedorService;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("title", "Lista de Productos");
        model.addAttribute("productos", productoService.listarProductos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("title", "Nuevo Producto");
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("title", "Editar Producto");
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        if (producto.getIdProducto() != null) {
            productoService.actualizar(producto);
        } else {
            productoService.registrar(producto);
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}
