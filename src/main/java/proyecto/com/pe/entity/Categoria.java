package proyecto.com.pe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")  // ⬅️ CRÍTICO: CON MAYÚSCULA
    private Integer idCategoria;

    @Column(name = "nombre")  // ⬅️ CRÍTICO
    private String nombre;

    @Column(name = "descripcion")  // ⬅️ CRÍTICO
    private String descripcion;
}