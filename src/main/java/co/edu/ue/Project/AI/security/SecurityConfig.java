package co.edu.ue.Project.AI.security;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/project-AI/usuarioAG").permitAll()
                .requestMatchers("/project-AI/adminAG").permitAll()
                .requestMatchers("/project-AI/admin").permitAll()
                .requestMatchers("/project-AI/login").permitAll()// ✅ Permitir registro sin autenticación
                .requestMatchers("/project-AI/usuarios").permitAll()
                .requestMatchers("/project-AI/usuarios/auth").permitAll()
                .requestMatchers("/project-AI/admin/login").permitAll()
                .requestMatchers("/project-AI/admin/auth").permitAll()
                .requestMatchers("/project-AI/eventos").permitAll()
                .requestMatchers("/project-AI/buscar").permitAll()
                .requestMatchers("/project-AI/eventosU").permitAll()
                .requestMatchers("/project-AI/eventos/buscar").permitAll()
                .requestMatchers("/project-AI/eventoComuAG").permitAll()
                .requestMatchers("/project-AI/eventosComunidadU").permitAll()
                .requestMatchers("/project-AI/eventoComu").permitAll()
                .requestMatchers("/project-AI/eventoComuTodo").permitAll()
                .requestMatchers("/project-AI/eventoComuE/{cod}").permitAll()
                .requestMatchers("/project-AI/eventoComu/{cod}").permitAll()
                .requestMatchers("/project-AI/eventoComuA").permitAll()
                .requestMatchers("/project-AI/eventoComuTodo").permitAll()
                .requestMatchers("/project-AI/eventoGuardadoAG").permitAll()
                .requestMatchers("/project-AI/eventoGuardadoUsuario").permitAll()
                .requestMatchers("/project-AI/compartir").permitAll()
                .requestMatchers("/project-AI/eventoComuBuscarFiltrar").permitAll()
                .requestMatchers("/project-AI/historial").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()); // ✅ Desactivar CSRF si usas frontend separado

        return http.build();
    }
}