package co.edu.ue.Project.AI.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ue.Project.AI.model.EventosCompartido;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IEventosCompartidoService;
import co.edu.ue.Project.AI.service.IUsuariosService;

@RestController
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")
public class EventosCompartidoController {

	@Autowired
	IEventosCompartidoService service;
	
	@Autowired
    IUsuariosService usuarioService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	//Tomar todos los datos
		@GetMapping(value="eventoComp", produces= MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<EventosCompartido>> getAllRegister(){
			//1 Enviar datos adicionales en la peticion del servicio
			//Cantidad de registros
			List<EventosCompartido> eventosCo = service.allDatos();
			
			//2.Crear header para añadir la info adicional
			HttpHeaders headers = new HttpHeaders();
			headers.add("cantidad_datos", String.valueOf(eventosCo.size()));
			headers.add("otro_dato", "datos");
			
			return new ResponseEntity<List<EventosCompartido>>(eventosCo, headers, HttpStatus.OK); 
			
		}
		//Borrar dato
		@DeleteMapping(value="eventoCompE/{cod}")
		public ResponseEntity<EventosCompartido> deleteDato(@PathVariable("cod") int eveCompId) {
			EventosCompartido eveEliminado = service.deleteEventoCompartido(eveCompId);
		    
		    	if (eveEliminado != null) {
		    		return new ResponseEntity<EventosCompartido>(eveEliminado, HttpStatus.OK);
		    	} else {
		    		return new ResponseEntity<EventosCompartido>(HttpStatus.NOT_FOUND);
		    	}
		    }
		
		//Buscar por id
		@GetMapping(value="eventoComp/{cod}", produces = MediaType.APPLICATION_JSON_VALUE) 
		public ResponseEntity<EventosCompartido> getDatoById(@PathVariable("cod") int id){
			return new ResponseEntity<EventosCompartido>(service.searchById(id),HttpStatus.ACCEPTED);
		}
		
	
		//Agregar datos
		@PostMapping(value="eventoCompAG", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Void> saveDato(@RequestBody EventosCompartido eventosCompartido){
			if(!service.registerEventoCompartido(eventosCompartido).equals(null)) {
				return new ResponseEntity<Void>(HttpStatus.CREATED);
			}else {
				return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		//Actualizar datos
		@PostMapping(value="eventoCompA", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Void> updateDato(@RequestBody EventosCompartido eventosCompartido){
			if(!service.updateEventoCompartido(eventosCompartido).equals(null)) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else {
				return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		// Compartir un evento (IA o Comunidad)
		@PostMapping("compartir")
		public ResponseEntity<?> compartirEvento(
		        @RequestBody EventosCompartido eventoCompartido,
		        @RequestHeader("Authorization") String authHeader) {

		    try {
		        // 1. Validar encabezado y extraer token
		        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
		        }

		        String token = authHeader.substring(7); // Quitar "Bearer "
		        String correoUsuario = jwtUtil.obtenerCorreoDesdeJWT(token);

		        if (correoUsuario == null) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
		        }

		        // 2. Buscar usuario por correo
		        Usuario usuario = usuarioService.findByEmail(correoUsuario);
		        if (usuario == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
		        }

		        // 3. Validar que se comparta solo un tipo de evento
		        boolean tieneEventoIA = eventoCompartido.getEvento() != null;
		        boolean tieneEventoComunidad = eventoCompartido.getEventosComunidad() != null;

		        if ((tieneEventoIA && tieneEventoComunidad) || (!tieneEventoIA && !tieneEventoComunidad)) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body("Debes compartir solo un tipo de evento: IA o Comunidad.");
		        }

		        // 4. Establecer fecha actual si no se especificó
		        if (eventoCompartido.getEveCompFecha() == null) {
		            eventoCompartido.setEveCompFecha(new Timestamp(System.currentTimeMillis()));
		        }

		        // 5. Asignar el usuario que compartió
		        eventoCompartido.setUsuario(usuario);

		        // 6. Guardar el evento compartido
		        EventosCompartido nuevoRegistro = service.registerEventoCompartido(eventoCompartido);

		        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("Error al compartir el evento: " + e.getMessage());
		    }
		}
		
	}
