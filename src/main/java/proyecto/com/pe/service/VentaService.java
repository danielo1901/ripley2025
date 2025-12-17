package proyecto.com.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto.com.pe.entity.Cliente;
import proyecto.com.pe.entity.DetalleVenta;
import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.entity.Venta;
import proyecto.com.pe.repository.ClienteRepository;
import proyecto.com.pe.repository.EmpleadoRepository;
import proyecto.com.pe.repository.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Autowired
    private DetalleVentaService detalleVentaService;

    // Registrar venta completa (venta + detalles)
    @Transactional
    public Venta registrarVentaCompleta(Venta venta, List<DetalleVenta> detalles) {
        // Validar que cliente existe y está activo
        Cliente cliente = clienteRepo.findById(venta.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getActivo()) {
            throw new RuntimeException("Cliente inactivo");
        }

        // Validar que empleado existe y está activo
        Empleado empleado = empleadoRepo.findById(venta.getEmpleado().getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if (!empleado.getActivo()) {
            throw new RuntimeException("Empleado inactivo");
        }

        // Establecer fecha actual si no viene
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }

        // Calcular total desde los detalles
        BigDecimal total = detalles.stream()
                .map(detalle -> detalle.getPrecioUnitario()
                        .multiply(new BigDecimal(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venta.setTotal(total);

        // Guardar venta
        Venta ventaGuardada = ventaRepo.save(venta);

        // Guardar detalles y actualizar stock
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaService.registrarConActualizacionStock(detalle);
        }

        return ventaGuardada;
    }

    // Registrar solo venta (sin detalles)
    @Transactional
    public Venta registrar(Venta venta) {
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }
        return ventaRepo.save(venta);
    }

    // Listar todas las ventas
    @Transactional(readOnly = true)
    public List<Venta> listarVentas() {
        return ventaRepo.findAll();
    }

    // Buscar venta por ID
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorId(Integer id) {
        return ventaRepo.findById(id);
    }

    // Buscar ventas por cliente
    @Transactional(readOnly = true)
    public List<Venta> buscarPorCliente(Integer idCliente) {
        return ventaRepo.findByClienteIdCliente(idCliente);
    }

    // Buscar ventas por empleado
    @Transactional(readOnly = true)
    public List<Venta> buscarPorEmpleado(Integer idEmpleado) {
        return ventaRepo.findByEmpleadoIdEmpleado(idEmpleado);
    }

    // Buscar ventas por rango de fechas
    @Transactional(readOnly = true)
    public List<Venta> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepo.findByFechaBetween(inicio, fin);
    }

    // Eliminar venta (CASCADE eliminará los detalles automáticamente)
    @Transactional
    public void eliminar(Integer id) {
        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        ventaRepo.delete(venta);
    }

    // Calcular total de ventas del día
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalDelDia(LocalDateTime fecha) {
        LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fecha.toLocalDate().atTime(23, 59, 59);

        List<Venta> ventasDelDia = ventaRepo.findByFechaBetween(inicioDia, finDia);

        return ventasDelDia.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}