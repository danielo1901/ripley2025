package proyecto.com.pe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.com.pe.entity.Proveedor;
import proyecto.com.pe.repository.ProveedorRepository;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepo;

    // Registrar proveedor
    @Transactional
    public Proveedor registrar(Proveedor proveedor) {
        if (proveedorRepo.existsByRuc(proveedor.getRuc())) {
            throw new RuntimeException("El RUC ya existe");
        }

        if (proveedor.getActivo() == null) {
            proveedor.setActivo(true);
        }

        return proveedorRepo.save(proveedor);
    }

    // Actualizar proveedor
    @Transactional
    public Proveedor actualizar(Proveedor proveedor) {
        Proveedor proveedorExistente = proveedorRepo.findById(proveedor.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Validar RUC si cambió
        if (!proveedorExistente.getRuc().equals(proveedor.getRuc())) {
            if (proveedorRepo.existsByRuc(proveedor.getRuc())) {
                throw new RuntimeException("El RUC ya existe");
            }
        }

        return proveedorRepo.save(proveedor);
    }

    // Listar todos los proveedores
    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedores() {
        return proveedorRepo.findAll();
    }

    // Buscar proveedor por ID
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorId(Integer id) {
        return proveedorRepo.findById(id);
    }

    // Buscar proveedor por RUC
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepo.findByRuc(ruc);
    }

    // Buscar proveedores activos
    @Transactional(readOnly = true)
    public List<Proveedor> buscarActivos() {
        return proveedorRepo.findByActivo(true);
    }

    // Buscar por nombre
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepo.findByNombreContainingIgnoreCase(nombre);
    }

    // Activar/Desactivar proveedor
    @Transactional
    public void cambiarEstado(Integer id, Boolean activo) {
        Proveedor proveedor = proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedor.setActivo(activo);
        proveedorRepo.save(proveedor);
    }

    // Eliminar proveedor
    @Transactional
    public void eliminar(Integer id) {
        Proveedor proveedor = proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedorRepo.delete(proveedor);
    }
}