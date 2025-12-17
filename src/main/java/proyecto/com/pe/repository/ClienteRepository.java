package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Buscar por DNI
    Optional<Cliente> findByDni(String dni);

    // Buscar por username
    Optional<Cliente> findByUsername(String username);

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Buscar clientes activos
    List<Cliente> findByActivo(Boolean activo);

    // Buscar por nombre o apellido (búsqueda parcial)
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);

    // Verificar si existe username (para validación)
    boolean existsByUsername(String username);

    // Verificar si existe email (para validación)
    boolean existsByEmail(String email);

    // Verificar si existe DNI (para validación)
    boolean existsByDni(String dni);
}