package co.edu.ue.Project.AI.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IUsuariosService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController 
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")


public class UsuariosController {
    
    @Autowired
    IUsuariosService service;
    @Autowired
	PasswordEncoder passwordEncoder;
    
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping(value="usuarios")
    public List<Usuario> getAllUsuarios() {
        return service.todasUsuarios();
    }
    
    @GetMapping(value="usuarios/{usu_id}")
    public Usuario getUsuariosById(@PathVariable int usu_id) {
        return service.buscarIdUsuarios(usu_id);
    }

    @PostMapping(value = "usuarioAG", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> saveDato(@RequestBody Usuario usuario) {
	    
	    usuario.setUsuPassword(passwordEncoder.encode(usuario.getUsuPassword()));
	    
	    Usuario nuevoUsuario = service.agregarUsuarios(usuario);
	    if (nuevoUsuario != null) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Registro exitoso");
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	    }
	}
    
    @PutMapping(value="usuarios")
    public Usuario putUsuarios(@RequestBody Usuario usuarios) {
        return service.actualizarUsuarios(usuarios);
    }
    
    @DeleteMapping(value = "/usuarios/{usu_id}")
    public ResponseEntity<String> deleteUsuarios(@PathVariable int usu_id) {
    boolean isDeleted = service.bajaUsuarios(usu_id);
    if (isDeleted) {
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }
    
}
    @GetMapping("/usuarios/auth")
    public ResponseEntity<Usuario> getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Si el código llega aquí, es porque Spring Security ya validó el token.
        // Simplemente obtenemos el usuario y lo devolvemos.
        Usuario usuario = service.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(usuario);
    }

    /**
     * ✅ Endpoint para login con JWT
     */
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> loginRequest) {
        Map<String, Object> response = new HashMap<>();

        if (!loginRequest.containsKey("email") || !loginRequest.containsKey("password")) {
            response.put("error", "Datos de login incompletos");
            return ResponseEntity.badRequest().body(response);
        }

        String email = loginRequest.get("email").toString();
        String passwordIngresada = loginRequest.get("password").toString();

        Usuario usuario = service.findByEmail(email);
        if (usuario == null) {
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (passwordEncoder.matches(passwordIngresada, usuario.getUsuPassword())) {
            // ✅ Generar token JWT
            String token = jwtUtil.generateToken(email);
            
            response.put("message", "Login exitoso");
            response.put("token", token);
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}