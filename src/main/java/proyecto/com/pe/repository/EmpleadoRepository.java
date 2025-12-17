package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    // Buscar por DNI
    Optional<Empleado> findByDni(String dni);

    // Buscar por username
    Optional<Empleado> findByUsername(String username);

    // Buscar empleados activos
    List<Empleado> findByActivo(Boolean activo);

    // Buscar por puesto
    List<Empleado> findByPuesto(String puesto);

    // Buscar por nombre o apellido (búsqueda parcial)
    List<Empleado> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);

    // Verificar si existe username (para validación)
    boolean existsByUsername(String username);

    // Verificar si existe DNI (para validación)
    boolean existsByDni(String dni);
}