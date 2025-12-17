package proyecto.com.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.DetalleVenta;
import proyecto.com.pe.service.DetalleVentaService;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-venta")
@CrossOrigin(origins = "*")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    // POST: Registrar detalle con actualización de stock
    @PostMapping
    public ResponseEntity<?> registrarDetalle(@RequestBody DetalleVenta detalle) {
        try {
            DetalleVenta detalleGuardado = detalleVentaService.registrarConActualizacionStock(detalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(detalleGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el detalle");
        }
    }

    // GET: Listar todos los detalles
    @GetMapping
    public ResponseEntity<?> listarDetalles() {
        try {
            List<DetalleVenta> detalles = detalleVentaService.listarDetalleVenta();
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar detalles");
        }
    }

    // GET: Buscar detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return detalleVentaService.buscarPorId(id)
                    .map(detalle -> ResponseEntity.ok(detalle))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el detalle");
        }
    }

    // GET: Buscar detalles por venta
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<?> buscarPorVenta(@PathVariable Integer idVenta) {
        try {
            List<DetalleVenta> detalles = detalleVentaService.buscarPorVenta(idVenta);
            if (detalles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron detalles para esta venta");
            }
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar detalles");
        }
    }

    // DELETE: Eliminar detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer id) {
        try {
            detalleVentaService.eliminar(id);
            return ResponseEntity.ok("Detalle eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el detalle");
        }
    }
}