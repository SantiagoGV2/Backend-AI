package co.edu.ue.Project.AI.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IEventosService;
import co.edu.ue.Project.AI.service.IUsuariosService;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestHeader;



@RestController 
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")

public class EventosController {
    
    @Autowired
    IEventosService service;
    @Autowired
    IUsuariosService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
  
    
    @GetMapping(value="eventos")
    public List<Evento> getAllEventos() {
        return service.todasEventos();
    }
    
    @GetMapping(value="eventos/{eve_id}")
    public ResponseEntity<?> getEventosById(@PathVariable int eve_id) {
        Evento evento = service.buscarIdEventos(eve_id);
        return evento != null ? ResponseEntity.ok(evento) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado.");
    }

    @PostMapping(value="eventos/add")
    public ResponseEntity<Evento> postEventos(@RequestBody Evento eventos){
        Evento eventoGuardado = service.agregarEventos(eventos);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoGuardado);
    }

    
    @PutMapping(value="eventos")
    public Evento putEventos(@RequestBody Evento eventos) {
        return service.actualizarEventos(eventos);
    }
    
    @DeleteMapping(value = "/eventos/{eve_id}")
    public ResponseEntity<String> deleteEventos(@PathVariable int eve_id) {
    boolean isDeleted = service.bajaEventos(eve_id);
    if (isDeleted) {
        return ResponseEntity.ok("Evento eliminado correctamente.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado.");
    }
}
    @PostMapping("buscar")
    public ResponseEntity<?> buscarYGuardarEventos(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {

        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            // Validar la consulta
            String consulta = request.get("consulta");
            if (consulta == null || consulta.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debe proporcionar una consulta válida."));
            }

            // Obtener usuario desde el token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o ausente."));
            }

            String jwt = token.substring(7);
            String correoUsuario = jwtUtil.obtenerCorreoDesdeJWT(jwt);

            Usuario usuario = usuarioService.findByEmail(correoUsuario);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado."));
            }

            List<Evento> eventos = service.buscarEventosDesdeWeb(consulta, usuario.getUsuId());

            if (eventos.isEmpty()) {
                return ResponseEntity.ok(Map.of("mensaje", "No se encontraron eventos para la consulta: " + consulta));
            }

            return ResponseEntity.ok(eventos);

        } catch (Exception e) {
            logger.error("Error al buscar y guardar eventos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor."));
        }
    }


    
    @GetMapping("eventosU")
    public ResponseEntity<List<Evento>> obtenerEventosPorUsuario(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = token.substring(7); // Extrae el token sin "Bearer "
        String correoUsuario = jwtUtil.obtenerCorreoDesdeJWT(jwt);

        if (correoUsuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.findByEmail(correoUsuario);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println("Usuario encontrado: " + usuario);
        System.out.println("ID del usuario: " + (usuario != null ? usuario.getUsuId() : "Usuario no encontrado"));
        List<Evento> eventos = service.obtenerEventosPorUsuario(usuario.getUsuId());
        return ResponseEntity.ok(eventos);
    }
}