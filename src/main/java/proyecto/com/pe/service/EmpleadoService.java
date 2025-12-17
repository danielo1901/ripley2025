package proyecto.com.pe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Autowired
    private AuthService authService;

    // Registrar empleado con password cifrado
    @Transactional
    public Empleado registrar(Empleado empleado) {
        // Validar que no exista el username
        if (empleadoRepo.existsByUsername(empleado.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        // Validar que no exista el DNI
        if (empleadoRepo.existsByDni(empleado.getDni())) {
            throw new RuntimeException("El DNI ya existe");
        }

        // Cifrar password
        String hashedPassword = authService.encryptPassword(empleado.getPasswordHash());
        empleado.setPasswordHash(hashedPassword);

        // Establecer activo por defecto
        if (empleado.getActivo() == null) {
            empleado.setActivo(true);
        }

        return empleadoRepo.save(empleado);
    }

    // Actualizar empleado
    @Transactional
    public Empleado actualizar(Empleado empleado) {
        Empleado empleadoExistente = empleadoRepo.findById(empleado.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Validar username si cambió
        if (!empleadoExistente.getUsername().equals(empleado.getUsername())) {
            if (empleadoRepo.existsByUsername(empleado.getUsername())) {
                throw new RuntimeException("El username ya existe");
            }
        }

        // Si viene una nueva password, cifrarla. Si no, mantener la anterior
        if (empleado.getPasswordHash() != null && !empleado.getPasswordHash().isEmpty()
                && !empleado.getPasswordHash().equals(empleadoExistente.getPasswordHash())) {
            String hashedPassword = authService.encryptPassword(empleado.getPasswordHash());
            empleado.setPasswordHash(hashedPassword);
        } else {
            empleado.setPasswordHash(empleadoExistente.getPasswordHash());
        }

        return empleadoRepo.save(empleado);
    }

    // Listar todos los empleados
    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        return empleadoRepo.findAll();
    }

    // Buscar empleado por ID
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorId(Integer id) {
        return empleadoRepo.findById(id);
    }

    // Buscar empleado por DNI
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorDni(String dni) {
        return empleadoRepo.findByDni(dni);
    }

    // Buscar empleado por username
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorUsername(String username) {
        return empleadoRepo.findByUsername(username);
    }

    // Buscar empleados activos
    @Transactional(readOnly = true)
    public List<Empleado> buscarActivos() {
        return empleadoRepo.findByActivo(true);
    }

    // Buscar por puesto
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorPuesto(String puesto) {
        return empleadoRepo.findByPuesto(puesto);
    }

    // Buscar por nombre o apellido
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorNombreOApellido(String busqueda) {
        return empleadoRepo.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(busqueda, busqueda);
    }

    // Activar/Desactivar empleado
    @Transactional
    public void cambiarEstado(Integer id, Boolean activo) {
        Empleado empleado = empleadoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        empleado.setActivo(activo);
        empleadoRepo.save(empleado);
    }

    // Eliminar empleado
    @Transactional
    public void eliminar(Integer id) {
        Empleado empleado = empleadoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        empleadoRepo.delete(empleado);
    }
}