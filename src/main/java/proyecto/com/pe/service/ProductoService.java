package proyecto.com.pe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.com.pe.entity.Producto;
import proyecto.com.pe.repository.ProductoRepository;
import proyecto.com.pe.repository.CategoriaRepository;
import proyecto.com.pe.repository.ProveedorRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @Autowired
    private ProveedorRepository proveedorRepo;

    // Registrar producto
    @Transactional
    public Producto registrar(Producto producto) {
        // Validar que exista la categoría
        if (!categoriaRepo.existsById(producto.getCategoria().getIdCategoria())) {
            throw new RuntimeException("Categoría no encontrada");
        }

        // Validar que exista el proveedor
        if (!proveedorRepo.existsById(producto.getProveedor().getIdProveedor())) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        return productoRepo.save(producto);
    }

    // Actualizar producto
    @Transactional
    public Producto actualizar(Producto producto) {
        if (!productoRepo.existsById(producto.getIdProducto())) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Validar que exista la categoría
        if (!categoriaRepo.existsById(producto.getCategoria().getIdCategoria())) {
            throw new RuntimeException("Categoría no encontrada");
        }

        // Validar que exista el proveedor
        if (!proveedorRepo.existsById(producto.getProveedor().getIdProveedor())) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        return productoRepo.save(producto);
    }

    // Listar todos los productos
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    // Buscar producto por ID
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepo.findById(id);
    }

    // Buscar productos por categoría
    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(Integer idCategoria) {
        return productoRepo.findByCategoriaIdCategoria(idCategoria);
    }

    // Buscar productos por proveedor
    @Transactional(readOnly = true)
    public List<Producto> buscarPorProveedor(Integer idProveedor) {
        return productoRepo.findByProveedorIdProveedor(idProveedor);
    }

    // Buscar productos por nombre
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepo.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar productos con stock bajo
    @Transactional(readOnly = true)
    public List<Producto> buscarStockBajo(Integer stockMinimo) {
        return productoRepo.findProductosConStockBajo(stockMinimo);
    }

    // Buscar productos sin stock
    @Transactional(readOnly = true)
    public List<Producto> buscarSinStock() {
        return productoRepo.findByStock(0);
    }

    // Actualizar stock
    @Transactional
    public void actualizarStock(Integer id, Integer nuevoStock) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        producto.setStock(nuevoStock);
        productoRepo.save(producto);
    }

    // Eliminar producto
    @Transactional
    public void eliminar(Integer id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepo.delete(producto);
    }
}