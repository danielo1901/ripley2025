package proyecto.com.pe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/img/**", "/webjars/**",
                                "/tienda")
                        .permitAll()
                        .requestMatchers("/productos/**", "/categorias/**", "/proveedores/**", "/clientes/**",
                                "/ventas/**", "/empleados/**")
                        .hasRole("EMPLEADO")
                        .requestMatchers("/tienda/comprar/**", "/tienda/mis-compras/**", "/mis-compras/**")
                        .hasRole("CLIENTE")
                        .requestMatchers("/").hasAnyRole("EMPLEADO", "CLIENTE")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            var roles = authentication.getAuthorities();
                            for (var role : roles) {
                                if (role.getAuthority().equals("ROLE_EMPLEADO")) {
                                    response.sendRedirect("/");
                                    return;
                                } else if (role.getAuthority().equals("ROLE_CLIENTE")) {
                                    response.sendRedirect("/tienda");
                                    return;
                                }
                            }
                            response.sendRedirect("/");
                        })
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/tienda")
                        .permitAll());

        return http.build();
    }
}