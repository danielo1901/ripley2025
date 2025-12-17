package proyecto.com.pe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.com.pe.entity.Producto;
import proyecto.com.pe.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // POST: Registrar producto
    @PostMapping
    public ResponseEntity<?> registrarProducto(@RequestBody Producto producto) {
        try {
            Producto productoGuardado = productoService.registrar(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar producto: " + e.getMessage());
        }
    }

    // PUT: Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        try {
            producto.setIdProducto(id);
            Producto productoActualizado = productoService.actualizar(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar producto: " + e.getMessage());
        }
    }

    // GET: Listar todos los productos
    @GetMapping
    public ResponseEntity<?> listarProductos() {
        try {
            List<Producto> productos = productoService.listarProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar productos");
        }
    }

    // GET: Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return productoService.buscarPorId(id)
                    .map(producto -> ResponseEntity.ok(producto))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar producto");
        }
    }

    // GET: Buscar productos por categoría
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<?> buscarPorCategoria(@PathVariable Integer idCategoria) {
        try {
            List<Producto> productos = productoService.buscarPorCategoria(idCategoria);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos");
        }
    }

    // GET: Buscar productos por proveedor
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<?> buscarPorProveedor(@PathVariable Integer idProveedor) {
        try {
            List<Producto> productos = productoService.buscarPorProveedor(idProveedor);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos");
        }
    }

    // GET: Buscar productos por nombre
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<Producto> productos = productoService.buscarPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos");
        }
    }

    // GET: Buscar productos con stock bajo
    @GetMapping("/stock-bajo")
    public ResponseEntity<?> buscarStockBajo(@RequestParam(defaultValue = "10") Integer minimo) {
        try {
            List<Producto> productos = productoService.buscarStockBajo(minimo);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos con stock bajo");
        }
    }

    // GET: Buscar productos sin stock
    @GetMapping("/sin-stock")
    public ResponseEntity<?> buscarSinStock() {
        try {
            List<Producto> productos = productoService.buscarSinStock();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos sin stock");
        }
    }

    // PATCH: Actualizar stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Integer id, @RequestParam Integer stock) {
        try {
            productoService.actualizarStock(id, stock);
            return ResponseEntity.ok("Stock actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar stock");
        }
    }

    // DELETE: Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok("Producto eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar producto");
        }
    }
}