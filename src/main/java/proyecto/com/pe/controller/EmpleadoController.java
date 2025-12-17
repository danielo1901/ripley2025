package proyecto.com.pe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    // POST: Registrar empleado
    @PostMapping
    public ResponseEntity<?> registrarEmpleado(@RequestBody Empleado empleado) {
        try {
            Empleado empleadoGuardado = empleadoService.registrar(empleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar empleado: " + e.getMessage());
        }
    }

    // PUT: Actualizar empleado
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleado) {
        try {
            empleado.setIdEmpleado(id);
            Empleado empleadoActualizado = empleadoService.actualizar(empleado);
            return ResponseEntity.ok(empleadoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar empleado: " + e.getMessage());
        }
    }

    // GET: Listar todos los empleados
    @GetMapping
    public ResponseEntity<?> listarEmpleados() {
        try {
            List<Empleado> empleados = empleadoService.listarEmpleados();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar empleados");
        }
    }

    // GET: Buscar empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return empleadoService.buscarPorId(id)
                    .map(empleado -> ResponseEntity.ok(empleado))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleado");
        }
    }

    // GET: Buscar empleado por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> buscarPorDni(@PathVariable String dni) {
        try {
            return empleadoService.buscarPorDni(dni)
                    .map(empleado -> ResponseEntity.ok(empleado))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleado");
        }
    }

    // GET: Buscar empleado por username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> buscarPorUsername(@PathVariable String username) {
        try {
            return empleadoService.buscarPorUsername(username)
                    .map(empleado -> ResponseEntity.ok(empleado))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleado");
        }
    }

    // GET: Listar empleados activos
    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<Empleado> empleados = empleadoService.buscarActivos();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar empleados activos");
        }
    }

    // GET: Buscar por puesto
    @GetMapping("/puesto/{puesto}")
    public ResponseEntity<?> buscarPorPuesto(@PathVariable String puesto) {
        try {
            List<Empleado> empleados = empleadoService.buscarPorPuesto(puesto);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleados");
        }
    }

    // GET: Buscar por nombre o apellido
    @GetMapping("/buscar/{texto}")
    public ResponseEntity<?> buscarPorNombreOApellido(@PathVariable String texto) {
        try {
            List<Empleado> empleados = empleadoService.buscarPorNombreOApellido(texto);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleados");
        }
    }

    // PATCH: Cambiar estado (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean activo) {
        try {
            empleadoService.cambiarEstado(id, activo);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar estado");
        }
    }

    // DELETE: Eliminar empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Integer id) {
        try {
            empleadoService.eliminar(id);
            return ResponseEntity.ok("Empleado eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar empleado");
        }
    }
}