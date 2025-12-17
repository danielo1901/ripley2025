package proyecto.com.pe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.com.pe.entity.Proveedor;
import proyecto.com.pe.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // POST: Registrar proveedor
    @PostMapping
    public ResponseEntity<?> registrarProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor proveedorGuardado = proveedorService.registrar(proveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(proveedorGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar proveedor: " + e.getMessage());
        }
    }

    // PUT: Actualizar proveedor
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        try {
            proveedor.setIdProveedor(id);
            Proveedor proveedorActualizado = proveedorService.actualizar(proveedor);
            return ResponseEntity.ok(proveedorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar proveedor: " + e.getMessage());
        }
    }

    // GET: Listar todos los proveedores
    @GetMapping
    public ResponseEntity<?> listarProveedores() {
        try {
            List<Proveedor> proveedores = proveedorService.listarProveedores();
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar proveedores");
        }
    }

    // GET: Buscar proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return proveedorService.buscarPorId(id)
                    .map(proveedor -> ResponseEntity.ok(proveedor))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar proveedor");
        }
    }

    // GET: Buscar proveedor por RUC
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<?> buscarPorRuc(@PathVariable String ruc) {
        try {
            return proveedorService.buscarPorRuc(ruc)
                    .map(proveedor -> ResponseEntity.ok(proveedor))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar proveedor");
        }
    }

    // GET: Listar proveedores activos
    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<Proveedor> proveedores = proveedorService.buscarActivos();
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar proveedores activos");
        }
    }

    // GET: Buscar por nombre
    @GetMapping("/buscar/{texto}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String texto) {
        try {
            List<Proveedor> proveedores = proveedorService.buscarPorNombre(texto);
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar proveedores");
        }
    }

    // PATCH: Cambiar estado (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean activo) {
        try {
            proveedorService.cambiarEstado(id, activo);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar estado");
        }
    }

    // DELETE: Eliminar proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Integer id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.ok("Proveedor eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar proveedor");
        }
    }
}