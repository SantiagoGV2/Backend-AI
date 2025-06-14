package co.edu.ue.Project.AI.security;

import co.edu.ue.Project.AI.service.IUsuariosService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
    private CustomUserDetailsService customUserDetailsService; 

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 1. Comprobar que la cabecera 'Authorization' existe y empieza con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.obtenerCorreoDesdeJWT(jwt);
        }

        // 2. Si obtuvimos un username y no hay una autenticación ya establecida en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los detalles del usuario desde la base de datos
        	UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            // 3. Validamos el token contra los detalles del usuario
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Si el token es válido, creamos una autenticación para Spring Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 4. Establecemos la autenticación en el contexto de seguridad.
                // ¡Este es el paso que "inicia sesión" para esta petición!
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("DEBUG: Usuario '" + username + "' autenticado y establecido en el contexto de seguridad.");
            }
        }
        
        // 5. Continuamos con la cadena de filtros de seguridad
        filterChain.doFilter(request, response);
    }
}