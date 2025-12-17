package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto.com.pe.entity.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar por categoría
    List<Producto> findByCategoriaIdCategoria(Integer idCategoria);

    // Buscar por proveedor
    List<Producto> findByProveedorIdProveedor(Integer idProveedor);

    // Buscar por nombre (búsqueda parcial)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);

    // Buscar productos sin stock
    List<Producto> findByStock(Integer stock);
}