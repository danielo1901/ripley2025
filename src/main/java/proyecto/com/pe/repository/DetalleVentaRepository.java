package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.DetalleVenta;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    List<DetalleVenta> findByVentaIdVenta(Integer idVenta);
}