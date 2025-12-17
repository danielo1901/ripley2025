package proyecto.com.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.com.pe.entity.Categoria;
import proyecto.com.pe.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria registrar(Categoria categoria) {
        // Validar que no exista una categoría con el mismo nombre
        Optional<Categoria> existe = categoriaRepository.findByNombre(categoria.getNombre());
        if (existe.isPresent()) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria categoria) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Verificar que el nuevo nombre no esté en uso por otra categoría
        Optional<Categoria> nombreExiste = categoriaRepository.findByNombre(categoria.getNombre());
        if (nombreExiste.isPresent() && !nombreExiste.get().getIdCategoria().equals(id)) {
            throw new RuntimeException("Ya existe otra categoría con ese nombre");
        }

        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
}