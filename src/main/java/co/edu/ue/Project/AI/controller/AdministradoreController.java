package co.edu.ue.Project.AI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ue.Project.AI.model.Administradore;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IAdministradoreService;


@RestController 
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")
public class AdministradoreController {

	@Autowired
	IAdministradoreService service;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@GetMapping(value="admin", produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Administradore>> getAllRegister(){
		//1 Enviar datos adicionales en la peticion del servicio
		//Cantidad de registros
		List<Administradore> adm = service.allDatos();
		
		//2.Crear header para añadir la info adicional
		HttpHeaders headers = new HttpHeaders();
		headers.add("cantidad_datos", String.valueOf(adm.size()));
		headers.add("otro_dato", "datos");
		
		return new ResponseEntity<List<Administradore>>(adm, headers, HttpStatus.OK); 
		
	}
    
	//Buscar por id
		@GetMapping(value="admin/{cod}", produces = MediaType.APPLICATION_JSON_VALUE) 
		public ResponseEntity<Administradore> getDatoById(@PathVariable("cod") int id){
			return new ResponseEntity<Administradore>(service.searchById(id),HttpStatus.ACCEPTED);
		}

    @PostMapping(value = "adminAG", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> saveDato(@RequestBody Administradore administrador) {
	    
    	administrador.setAdmPassword(passwordEncoder.encode(administrador.getAdmPassword()));
	    
    	Administradore nuevoUsuario = service.registerAdministrador(administrador);
	    if (nuevoUsuario != null) {
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Registro exitoso");
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	    }
	}
    
    @PostMapping(value="adminA", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateDato(@RequestBody Administradore administrador){
		if(!service.updateAdministrador(administrador).equals(null)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
    
    @DeleteMapping(value="adminE/{cod}")
	public ResponseEntity<Administradore> deleteDato(@PathVariable("cod") int admId) {
    	Administradore admEliminado = service.deleteAdministrador(admId);
	    
	    	if (admEliminado != null) {
	    		return new ResponseEntity<Administradore>(admEliminado, HttpStatus.OK);
	    	} else {
	    		return new ResponseEntity<Administradore>(HttpStatus.NOT_FOUND);
	    	}
	}
    
    @GetMapping(value = "admin/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthenticatedUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado o inválido");
        }

        try {
            // Eliminar "Bearer " para extraer el token puro
            String jwtToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwtToken);

            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
            }

            Administradore administrador = service.findByEmail(username);
            if (administrador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador no encontrado");
            }

            return ResponseEntity.ok(administrador);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al procesar el token");
        }
    }



    /**
     * ✅ Endpoint para login con JWT
     */
    @PostMapping(value = "admin/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> loginRequest) {
        Map<String, Object> response = new HashMap<>();

        if (!loginRequest.containsKey("email") || !loginRequest.containsKey("password")) {
            response.put("error", "Datos de login incompletos");
            return ResponseEntity.badRequest().body(response);
        }

        String email = loginRequest.get("email").toString();
        String passwordIngresada = loginRequest.get("password").toString();

        Administradore administrador = service.findByEmail(email);
        if (administrador == null) {
            response.put("error", "Administrador no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (passwordEncoder.matches(passwordIngresada, administrador.getAdmPassword())) {
            // ✅ Generar token JWT
            String token = jwtUtil.generateToken(email);
            
            response.put("message", "Login exitoso");
            response.put("token", token);
            response.put("administrador", administrador);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}