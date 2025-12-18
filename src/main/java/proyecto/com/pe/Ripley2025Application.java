package proyecto.com.pe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import proyecto.com.pe.entity.Empleado;
import proyecto.com.pe.repository.EmpleadoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class Ripley2025Application {

	public static void main(String[] args) {
		SpringApplication.run(Ripley2025Application.class, args);
	}

	@Bean
	public CommandLineRunner initData(EmpleadoRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.count() == 0) {
				Empleado admin = new Empleado();
				admin.setNombre("Administrador");
				admin.setApellido("Sistema");
				admin.setDni("00000000");
				admin.setFechaIngreso(LocalDate.now());
				admin.setPuesto("ADMIN");
				admin.setSalario(new BigDecimal("5000.00"));
				admin.setUsername("admin");
				admin.setPasswordHash(encoder.encode("admin123"));
				admin.setActivo(true);
				repo.save(admin);
				System.out.println(">>> Usuario administrador creado: admin / admin123");
			}
		};
	}

}
