package proyecto.com.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.com.pe.dto.LoginRequest;
import proyecto.com.pe.dto.LoginResponse;
import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.repository.ClienteRepository;
import proyecto.com.pe.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienterepo;

    @Autowired
    private EmpleadoRepository empleadorepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Cifrado de contraseña usando BCrypt

    public String encryptPassword(String plainPassword)
    {
        return passwordEncoder.encode(plainPassword);
    }

    //Verificar si la contraseña coincide

    public Boolean verifyPassword(String plainPassword, String hashedPassword)
    {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    //Login del cliente
    @Transactional(readOnly = true)
    public LoginResponse loginCliente(LoginRequest request)
    {
        Optional<Cliente> clienteOpt = clienterepo.findByUsername(request.getUsername());

        if(clienteOpt.isEmpty())
        {
            return new LoginResponse(false, "Usuario no encontrado");
        }
        Cliente cliente = clienteOpt.get();

        if(!cliente.getActivo())
        {
            return new LoginResponse(false, "Usuario inactivo");
        }

        if(!verifyPassword(request.getPassword(),cliente.getPasswordHash()))
        {
            return new LoginResponse(false,"Contraseña incorrecta");
        }

        return new LoginResponse(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                "CLIENTE"
        );
    }


    //Login para empleado
    @Transactional(readOnly = true)
    public LoginResponse loginEmpleado(LoginRequest request)
    {
        Optional <Empleado> empleadoOpt = empleadorepo.findByUsername(request.getUsername());

        if(empleadoOpt.isEmpty())
        {
            return new LoginResponse(false,"Empleado no encontrado");
        }
        Empleado empleado = empleadoOpt.get();

        if(!empleado.getActivo())
        {
            return new LoginResponse(false, "Emplado inactivo");
        }

        if(!verifyPassword(request.getPassword(), empleado.getPasswordHash()))
        {
            return new LoginResponse(false, "Constraseña incorrecta");
        }

        return new LoginResponse(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                empleado.getApellido(),
                null, // para que no se cuelgue(no es not null en la bd)
                "EMPLEADO",
                empleado.getPuesto()
        );
    }

    /**
     * Login general (intenta cliente primero, luego empleado)
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        LoginResponse clienteResponse = loginCliente(request);
        if (clienteResponse.isSuccess()) {
            return clienteResponse;
        }


        LoginResponse empleadoResponse = loginEmpleado(request);
        if (empleadoResponse.isSuccess()) {
            return empleadoResponse;
        }

        return new LoginResponse(false, "Credenciales inválidas");
    }



}
