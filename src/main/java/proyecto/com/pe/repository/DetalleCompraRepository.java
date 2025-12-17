package proyecto.com.pe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
    List<DetalleCompra> findByCompraIdCompra(Integer idCompra);
}