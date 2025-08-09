package co.edu.ue.Project.AI.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // ✅ Permite que Spring lo inyecte automáticamente
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secretKey; 
	@Value("${jwt.expiration.ms}")
	private long expirationTime;
    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token) // Verifica el token
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null; // Devuelve null en lugar de lanzar una excepción
        }
    }

    public String obtenerCorreoDesdeJWT(String jwt) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
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
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
