package proyecto.com.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.com.pe.dto.LoginRequest;
import proyecto.com.pe.dto.LoginResponse;
import proyecto.com.pe.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Login general (cliente o empleado)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Error en el servidor: " + e.getMessage()));
        }
    }

    /**
     * Login específico para clientes
     * POST /api/auth/login/cliente
     */
    @PostMapping("/login/cliente")
    public ResponseEntity<LoginResponse> loginCliente(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginCliente(request);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Error en el servidor: " + e.getMessage()));
        }
    }

    /**
     * Login específico para empleados
     * POST /api/auth/login/empleado
     */
    @PostMapping("/login/empleado")
    public ResponseEntity<LoginResponse> loginEmpleado(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginEmpleado(request);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Error en el servidor: " + e.getMessage()));
        }
    }
}