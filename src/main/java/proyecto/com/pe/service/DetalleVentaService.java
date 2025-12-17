package proyecto.com.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto.com.pe.entity.DetalleVenta;
import proyecto.com.pe.entity.Producto;
import proyecto.com.pe.repository.DetalleVentaRepository;
import proyecto.com.pe.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleRepo;

    @Autowired
    private ProductoRepository productoRepo;

    // Registrar detalle Y actualizar stock del producto
    @Transactional
    public DetalleVenta registrarConActualizacionStock(DetalleVenta detalle) {
        // Obtener producto
        Producto producto = productoRepo.findById(detalle.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar stock suficiente
        if (producto.getStock() < detalle.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        // Actualizar stock
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoRepo.save(producto);

        // Guardar detalle
        return detalleRepo.save(detalle);
    }

    // Registrar detalle simple (sin actualizar stock)
    @Transactional
    public DetalleVenta registrar(DetalleVenta detalle) {
        return detalleRepo.save(detalle);
    }

    // Listar todos los detalles
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarDetalleVenta() {
        return detalleRepo.findAll();
    }

    // Buscar detalles por venta
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorVenta(Integer idVenta) {
        return detalleRepo.findByVentaIdVenta(idVenta);
    }

    // Buscar detalle por ID
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        return detalleRepo.findById(id);
    }

    // Eliminar detalle
    @Transactional
    public void eliminar(Integer id) {
        DetalleVenta detalle = detalleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        detalleRepo.delete(detalle);
    }
}