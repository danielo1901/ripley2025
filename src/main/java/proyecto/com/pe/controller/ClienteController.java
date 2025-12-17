package proyecto.com.pe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // POST: Registrar cliente
    @PostMapping
    public ResponseEntity<?> registrarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteGuardado = clienteService.registrar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar cliente: " + e.getMessage());
        }
    }

    // PUT: Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        try {
            cliente.setIdCliente(id);
            Cliente clienteActualizado = clienteService.actualizar(cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar cliente: " + e.getMessage());
        }
    }

    // GET: Listar todos los clientes
    @GetMapping
    public ResponseEntity<?> listarClientes() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar clientes");
        }
    }

    // GET: Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return clienteService.buscarPorId(id)
                    .map(cliente -> ResponseEntity.ok(cliente))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar cliente");
        }
    }

    // GET: Buscar cliente por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> buscarPorDni(@PathVariable String dni) {
        try {
            return clienteService.buscarPorDni(dni)
                    .map(cliente -> ResponseEntity.ok(cliente))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar cliente");
        }
    }

    // GET: Buscar cliente por username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> buscarPorUsername(@PathVariable String username) {
        try {
            return clienteService.buscarPorUsername(username)
                    .map(cliente -> ResponseEntity.ok(cliente))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar cliente");
        }
    }

    // GET: Listar clientes activos
    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<Cliente> clientes = clienteService.buscarActivos();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar clientes activos");
        }
    }

    // GET: Buscar por nombre o apellido
    @GetMapping("/buscar/{texto}")
    public ResponseEntity<?> buscarPorNombreOApellido(@PathVariable String texto) {
        try {
            List<Cliente> clientes = clienteService.buscarPorNombreOApellido(texto);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar clientes");
        }
    }

    // PATCH: Cambiar estado (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean activo) {
        try {
            clienteService.cambiarEstado(id, activo);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar estado");
        }
    }

    // DELETE: Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {
        try {
            clienteService.eliminar(id);
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar cliente");
        }
    }
}