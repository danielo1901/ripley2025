package proyecto.com.pe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String tipo; //Para poder diferencias entre cliente y empleado
    private String puesto;
    private boolean success;
    private String message;

    //Agregamos el constructor para cliente
    public LoginResponse(Integer id, String nombre, String apellido, String email, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipo = tipo;
        this.success = true;
        this.message = "Login exitoso";
    }
    // Constructor para éxito de empleado
    public LoginResponse(Integer id, String nombre, String apellido, String email, String tipo, String puesto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipo = tipo;
        this.puesto = puesto;
        this.success = true;
        this.message = "Login exitoso";
    }

    // Constructor para error
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


}
