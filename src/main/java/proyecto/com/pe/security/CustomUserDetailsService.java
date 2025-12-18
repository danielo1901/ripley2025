package proyecto.com.pe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.repository.ClienteRepository;
import proyecto.com.pe.repository.EmpleadoRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Intentar buscar en Clientes
        Optional<Cliente> clienteOpt = clienteRepo.findByUsername(username);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            return User.builder()
                    .username(cliente.getUsername())
                    .password(cliente.getPasswordHash())
                    .roles("CLIENTE")
                    .build();
        }

        // Intentar buscar en Empleados
        Optional<Empleado> empleadoOpt = empleadoRepo.findByUsername(username);
        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            return User.builder()
                    .username(empleado.getUsername())
                    .password(empleado.getPasswordHash())
                    .roles("EMPLEADO")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
