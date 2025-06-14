package co.edu.ue.Project.AI.controller;

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

import co.edu.ue.Project.AI.dto.EventosGuardadoDTO;
import co.edu.ue.Project.AI.model.EventosGuardado;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IEventosGuardadoService;
import co.edu.ue.Project.AI.service.IUsuariosService;

@RestController
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")
public class EventosGuardadoController {

	@Autowired
	IEventosGuardadoService service;
	
	@Autowired
    IUsuariosService usuarioService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	
	//Tomar todos los datos
		@GetMapping(value="eventoGuardado", produces= MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<EventosGuardado>> getAllRegister(){
			//1 Enviar datos adicionales en la peticion del servicio
			//Cantidad de registros
			List<EventosGuardado> eventosG = service.allDatos();
			
			//2.Crear header para añadir la info adicional
			HttpHeaders headers = new HttpHeaders();
			headers.add("cantidad_datos", String.valueOf(eventosG.size()));
			headers.add("otro_dato", "datos");
			
			return new ResponseEntity<List<EventosGuardado>>(eventosG, headers, HttpStatus.OK); 
			
		}
		//Borrar dato
		@DeleteMapping(value="eventoGuardadoE/{cod}")
		public ResponseEntity<EventosGuardado> deleteDato(@PathVariable("cod") int eveGuaId) {
			EventosGuardado eveEliminado = service.deleteEventosGuardado(eveGuaId);
		    
		    	if (eveEliminado != null) {
		    		return new ResponseEntity<EventosGuardado>(eveEliminado, HttpStatus.OK);
		    	} else {
		    		return new ResponseEntity<EventosGuardado>(HttpStatus.NOT_FOUND);
		    	}
		    }
		
		//Buscar por id
		@GetMapping(value="eventoGuardado/{cod}", produces = MediaType.APPLICATION_JSON_VALUE) 
		public ResponseEntity<EventosGuardado> getDatoById(@PathVariable("cod") int id){
			return new ResponseEntity<EventosGuardado>(service.searchById(id),HttpStatus.ACCEPTED);
		}
		
		//Actualizar datos
		@PostMapping(value="eventoGuardadoA", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Void> updateDato(@RequestBody EventosGuardado eventosGuardado){
			if(!service.updateEventosGuardado(eventosGuardado).equals(null)) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else {
				return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
	
		//Agregar datos
		@PostMapping(value = "eventoGuardadoAG", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Void> saveEventoGuardado(
		        @RequestBody EventosGuardado evento,
		        @RequestHeader("Authorization") String token) {

		    String email = jwtUtil.obtenerCorreoDesdeJWT(token.replace("Bearer ", ""));

		    if (email != null) {
		        Usuario usuario = usuarioService.findByEmail(email); // Asegúrate que este método existe
		        if (usuario != null) {
		            // Verificar si el evento ya está guardado para el usuario
		        	boolean existe = service.eventoGuardadoExiste(
		        		    usuario.getUsuId(),
		        		    evento.getEvento() != null ? evento.getEvento().getEveId() : 0,
		        		    evento.getEventosComunidad() != null ? evento.getEventosComunidad().getEveComuId() : 0
		        		);
		            if (existe) {
		                // Si ya existe, retornamos un código 409 Conflict
		                return new ResponseEntity<>(HttpStatus.CONFLICT);
		            }
		            
		            evento.setUsuario(usuario); // Asigna el usuario al evento
		            service.registerEventosGuardado(evento);
		            return new ResponseEntity<>(HttpStatus.CREATED);
		        }
		    }

		    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		@GetMapping("eventoGuardadoUsuario")
		public ResponseEntity<List<EventosGuardadoDTO>> getEventosGuardadosByToken(
		        @RequestHeader("Authorization") String token) {

		    String email = jwtUtil.obtenerCorreoDesdeJWT(token.replace("Bearer ", ""));

		    if (email != null) {
		        Usuario usuario = usuarioService.findByEmail(email);
		        if (usuario != null) {
		            List<EventosGuardadoDTO> eventosDto = service.findDetallesEventosGuardadosByUsuarioId(usuario.getUsuId());
		            return new ResponseEntity<>(eventosDto, HttpStatus.OK);
		        }
		    }

		    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}