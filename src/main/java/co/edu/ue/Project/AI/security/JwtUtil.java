package co.edu.ue.Project.AI.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // ✅ Permite que Spring lo inyecte automáticamente
public class JwtUtil {
    private static final String SECRET_KEY = "secreto123";
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token) // Verifica el token
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null; // Devuelve null en lugar de lanzar una excepción
        }
    }

    public String obtenerCorreoDesdeJWT(String jwt) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(jwt) // Verifica el token
                    .getSubject(); // Obtiene el correo electrónico (subject)
        } catch (JWTVerificationException e) {
            return null; // Devuelve null si el token no es válido
        }
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public Date extractExpiration(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getExpiresAt();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
