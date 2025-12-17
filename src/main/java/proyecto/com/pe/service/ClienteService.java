package proyecto.com.pe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private AuthService authService;

    // Registrar cliente con password cifrado
    @Transactional
    public Cliente registrar(Cliente cliente) {
        // Validar que no exista el username
        if (clienteRepo.existsByUsername(cliente.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        // Validar que no exista el email
        if (cliente.getEmail() != null && clienteRepo.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        // Validar que no exista el DNI
        if (clienteRepo.existsByDni(cliente.getDni())) {
            throw new RuntimeException("El DNI ya existe");
        }

        // Cifrar password
        String hashedPassword = authService.encryptPassword(cliente.getPasswordHash());
        cliente.setPasswordHash(hashedPassword);

        // Establecer activo por defecto
        if (cliente.getActivo() == null) {
            cliente.setActivo(true);
        }

        return clienteRepo.save(cliente);
    }

    // Actualizar cliente
    @Transactional
    public Cliente actualizar(Cliente cliente) {
        Cliente clienteExistente = clienteRepo.findById(cliente.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar username si cambió
        if (!clienteExistente.getUsername().equals(cliente.getUsername())) {
            if (clienteRepo.existsByUsername(cliente.getUsername())) {
                throw new RuntimeException("El username ya existe");
            }
        }

        // Validar email si cambió
        if (cliente.getEmail() != null && !clienteExistente.getEmail().equals(cliente.getEmail())) {
            if (clienteRepo.existsByEmail(cliente.getEmail())) {
                throw new RuntimeException("El email ya existe");
            }
        }

        // Si viene una nueva password, cifrarla. Si no, mantener la anterior
        if (cliente.getPasswordHash() != null && !cliente.getPasswordHash().isEmpty()
                && !cliente.getPasswordHash().equals(clienteExistente.getPasswordHash())) {
            String hashedPassword = authService.encryptPassword(cliente.getPasswordHash());
            cliente.setPasswordHash(hashedPassword);
        } else {
            cliente.setPasswordHash(clienteExistente.getPasswordHash());
        }

        return clienteRepo.save(cliente);
    }

    // Listar todos los clientes
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepo.findAll();
    }

    // Buscar cliente por ID
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepo.findById(id);
    }

    // Buscar cliente por DNI
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepo.findByDni(dni);
    }

    // Buscar cliente por username
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorUsername(String username) {
        return clienteRepo.findByUsername(username);
    }

    // Buscar clientes activos
    @Transactional(readOnly = true)
    public List<Cliente> buscarActivos() {
        return clienteRepo.findByActivo(true);
    }

    // Buscar por nombre o apellido
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombreOApellido(String busqueda) {
        return clienteRepo.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(busqueda, busqueda);
    }

    // Activar/Desactivar cliente
    @Transactional
    public void cambiarEstado(Integer id, Boolean activo) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setActivo(activo);
        clienteRepo.save(cliente);
    }

    // Eliminar cliente
    @Transactional
    public void eliminar(Integer id) {
        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepo.delete(cliente);
    }
}