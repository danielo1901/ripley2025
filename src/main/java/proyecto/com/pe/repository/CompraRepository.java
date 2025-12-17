package proyecto.com.pe.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByProveedorIdProveedor(Integer idProveedor);
    List<Compra> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}