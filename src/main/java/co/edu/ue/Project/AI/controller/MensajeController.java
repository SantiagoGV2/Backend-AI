package co.edu.ue.Project.AI.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IMensajeService;
import co.edu.ue.Project.AI.service.IUsuariosService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "project-AI/mensajes")
@CrossOrigin(origins = "*")
public class MensajeController {
	
	@Autowired
	IMensajeService service;
	
	@Autowired
    private IUsuariosService usuarioService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping(value="eventos/add")
    public ResponseEntity<Mensaje> postEventos(@RequestBody Mensaje mensaje){
		Mensaje mensajeGuardado = service.addMensajes(mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeGuardado);
    }
	
	@GetMapping(value="mensajes")
    public List<Mensaje> getAllMensajes() {
        return service.getAllMensajes();
    }
    
    @GetMapping(value="mensajes/{msg_id}")
    public Mensaje getMensajeById(@PathVariable int msg_id) {
        return service.getIdMensaje(msg_id);
    }

    
    @PutMapping(value="mensajesA")
    public Mensaje putMensajes(@RequestBody Mensaje mensajes) {
        return service.uppMensajes(mensajes);
    }
    
    @DeleteMapping(value = "/mensajes/{msg_id}")
    public ResponseEntity<String> deleteUsuarios(@PathVariable int msg_id) {
    boolean isDeleted = service.deleteMensaje(msg_id);
    if (isDeleted) {
        return ResponseEntity.ok("Mensaje eliminado correctamente.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }
    
}
    /**
     * ✅ Obtener mensajes entre dos usuarios autenticados
     */
    @GetMapping("/conversacion/{idReceptor}")
    public ResponseEntity<List<Mensaje>> obtenerConversacion(
            @PathVariable int idReceptor, HttpServletRequest request) {

        // Extraer el usuario autenticado desde JWT
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String emailEmisor = jwtUtil.extractUsername(token);

        // Buscar el ID del usuario autenticado
        Usuario emisor = usuarioService.findByEmail(emailEmisor);
        if (emisor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Buscar el receptor
        Usuario receptor = usuarioService.buscarIdUsuarios(idReceptor);
        if (receptor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Mensaje> mensajes = service.obtenerMensajesEntreUsuarios(emisor, receptor);
        return ResponseEntity.ok(mensajes);
    }

    /**
     * ✅ Obtener TODOS los mensajes del usuario autenticado
     */
    @GetMapping("/usuario")
    public ResponseEntity<List<Mensaje>> obtenerMensajesUsuario(HttpServletRequest request) {

        // Extraer el usuario autenticado desde JWT
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String emailUsuario = jwtUtil.extractUsername(token);

        // Buscar el ID del usuario autenticado
        Usuario usuario = usuarioService.findByEmail(emailUsuario);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Mensaje> mensajes = service.obtenerMensajesDeUsuario(usuario);
        return ResponseEntity.ok(mensajes);
    }

}
