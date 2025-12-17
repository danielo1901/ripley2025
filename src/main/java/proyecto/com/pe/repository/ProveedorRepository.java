package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    // Buscar por RUC
    Optional<Proveedor> findByRuc(String ruc);

    // Buscar proveedores activos
    List<Proveedor> findByActivo(Boolean activo);

    // Buscar por nombre (búsqueda parcial)
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    // Verificar si existe RUC
    boolean existsByRuc(String ruc);
}