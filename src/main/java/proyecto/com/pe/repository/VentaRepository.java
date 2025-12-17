package proyecto.com.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.com.pe.entity.Venta;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByClienteIdCliente(Integer idCliente);
    List<Venta> findByEmpleadoIdEmpleado(Integer idEmpleado);
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}