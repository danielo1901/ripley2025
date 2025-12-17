package proyecto.com.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.com.pe.entity.DetalleVenta;
import proyecto.com.pe.entity.Venta;
import proyecto.com.pe.service.VentaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // DTO para recibir venta completa con sus detalles
    public static class VentaCompletaDTO {
        private Venta venta;
        private List<DetalleVenta> detalles;

        public Venta getVenta() { return venta; }
        public void setVenta(Venta venta) { this.venta = venta; }
        public List<DetalleVenta> getDetalles() { return detalles; }
        public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
    }

    // POST: Registrar venta completa (venta + detalles)
    @PostMapping("/completa")
    public ResponseEntity<?> registrarVentaCompleta(@RequestBody VentaCompletaDTO ventaCompleta) {
        try {
            Venta ventaGuardada = ventaService.registrarVentaCompleta(
                    ventaCompleta.getVenta(),
                    ventaCompleta.getDetalles()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaGuardada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la venta");
        }
    }

    // POST: Registrar solo venta (sin detalles)
    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody Venta venta) {
        try {
            Venta ventaGuardada = ventaService.registrar(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaGuardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la venta: " + e.getMessage());
        }
    }

    // GET: Listar todas las ventas
    @GetMapping
    public ResponseEntity<?> listarVentas() {
        try {
            List<Venta> ventas = ventaService.listarVentas();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar ventas");
        }
    }

    // GET: Buscar venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ventaService.buscarPorId(id)
                    .map(venta -> ResponseEntity.ok(venta))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la venta");
        }
    }

    // GET: Buscar ventas por cliente
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> buscarPorCliente(@PathVariable Integer idCliente) {
        try {
            List<Venta> ventas = ventaService.buscarPorCliente(idCliente);
            if (ventas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron ventas para este cliente");
            }
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar ventas");
        }
    }

    // GET: Buscar ventas por empleado
    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<?> buscarPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            List<Venta> ventas = ventaService.buscarPorEmpleado(idEmpleado);
            if (ventas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron ventas para este empleado");
            }
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar ventas");
        }
    }

    // GET: Buscar ventas por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<?> buscarPorFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {
        try {
            LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
            LocalDateTime fechaFin = LocalDateTime.parse(fin);

            List<Venta> ventas = ventaService.buscarPorFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al buscar ventas: " + e.getMessage());
        }
    }

    // GET: Calcular total de ventas del día
    @GetMapping("/total-dia")
    public ResponseEntity<?> calcularTotalDelDia(@RequestParam String fecha) {
        try {
            LocalDateTime fechaConsulta = LocalDateTime.parse(fecha);
            BigDecimal total = ventaService.calcularTotalDelDia(fechaConsulta);

            TotalDiaResponse response = new TotalDiaResponse(
                    fechaConsulta.toLocalDate().toString(),
                    total
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al calcular total: " + e.getMessage());
        }
    }

    // DELETE: Eliminar venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Integer id) {
        try {
            ventaService.eliminar(id);
            return ResponseEntity.ok("Venta eliminada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la venta");
        }
    }

    // Clase interna para respuesta de total del día
    static class TotalDiaResponse {
        private String fecha;
        private BigDecimal total;

        public TotalDiaResponse(String fecha, BigDecimal total) {
            this.fecha = fecha;
            this.total = total;
        }

        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }
}