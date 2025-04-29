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

import co.edu.ue.Project.AI.model.Administradore;
import co.edu.ue.Project.AI.model.EventosComunidad;
import co.edu.ue.Project.AI.security.JwtUtil;
import co.edu.ue.Project.AI.service.IAdministradoreService;
import co.edu.ue.Project.AI.service.IEventosComunidadService;

@RestController
@RequestMapping(value = "project-AI")
@CrossOrigin(origins = "*")
public class EventosComunidadController {
	
	@Autowired
	IEventosComunidadService service;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	IAdministradoreService administradorService;
	
	//Tomar todos los datos
	@GetMapping(value="eventoComuTodo", produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventosComunidad>> getAllRegister(){
		//1 Enviar datos adicionales en la peticion del servicio
		//Cantidad de registros
		List<EventosComunidad> eventosC = service.allDatos();
				
		//2.Crear header para añadir la info adicional
		HttpHeaders headers = new HttpHeaders();
		headers.add("cantidad_datos", String.valueOf(eventosC.size()));
		headers.add("otro_dato", "datos");
				
		return new ResponseEntity<List<EventosComunidad>>(eventosC, headers, HttpStatus.OK); 
				
	}
	
	//Tomar todos los datos por administrador
	@GetMapping(value="eventoComu", produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EventosComunidad>> getAllRegister(@RequestHeader("Authorization") String token) {
	    if (token == null || !token.startsWith("Bearer ")) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    String jwt = token.substring(7);
	    String correoAdministrador = jwtUtil.obtenerCorreoDesdeJWT(jwt);

	    if (correoAdministrador == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    Administradore administrador = administradorService.findByEmail(correoAdministrador);
	    if (administrador == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    List<EventosComunidad> eventos = service.obtenerEventosComunidadPorAdministrador(administrador.getAdmId());

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("cantidad_datos", String.valueOf(eventos.size()));

	    return new ResponseEntity<>(eventos, headers, HttpStatus.OK);
	}


	//Borrar dato
	@DeleteMapping(value="eventoComuE/{cod}")
	public ResponseEntity<Void> deleteDato(@RequestHeader("Authorization") String token,
	                                       @PathVariable("cod") int eveComuId) {
	    if (token == null || !token.startsWith("Bearer ")) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    String jwt = token.substring(7);
	    String correoAdministrador = jwtUtil.obtenerCorreoDesdeJWT(jwt);

	    if (correoAdministrador == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    Administradore administrador = administradorService.findByEmail(correoAdministrador);
	    if (administrador == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    EventosComunidad evento = service.searchById(eveComuId);
	    if (evento == null || evento.getAdministradore().getAdmId() != administrador.getAdmId()) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // no es dueño del recurso
	    }

	    service.deleteEventosComunidad(eveComuId);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	//Buscar por id
	@GetMapping(value="eventoComu/{cod}", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<EventosComunidad> getDatoById(@PathVariable("cod") int id){
		return new ResponseEntity<EventosComunidad>(service.searchById(id),HttpStatus.ACCEPTED);
	}
	
	//Buscar por Titulo
	@GetMapping(value="eventoComuT/{title}", produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<EventosComunidad> getDatoByTitulo(@PathVariable("title") String titulo){
		return new ResponseEntity<EventosComunidad>(service.searchByTitulo(titulo),HttpStatus.ACCEPTED);
	}
	
	//Agregar datos
	@PostMapping(value = "eventoComuAG", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveDato(@RequestHeader("Authorization") String token,
	                                     @RequestBody EventosComunidad eventosComunidad) {
	    if (token == null || !token.startsWith("Bearer ")) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    String jwt = token.substring(7); // Quita el "Bearer "
	    String correoAdministrador = jwtUtil.obtenerCorreoDesdeJWT(jwt);

	    if (correoAdministrador == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    Administradore administrador = administradorService.findByEmail(correoAdministrador);
	    if (administrador == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    eventosComunidad.setAdministradore(administrador);

	    if (service.registerEventosComunidad(eventosComunidad) != null) {
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	    }
	}


	
	//Actualizar datos
	@PostMapping(value = "eventoComuA", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateDato(@RequestHeader("Authorization") String token,
	                                       @RequestBody EventosComunidad eventosComunidad) {
	    if (token == null || !token.startsWith("Bearer ")) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    String jwt = token.substring(7); // Quita el "Bearer "
	    String correoAdministrador = jwtUtil.obtenerCorreoDesdeJWT(jwt);

	    if (correoAdministrador == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    Administradore administrador = administradorService.findByEmail(correoAdministrador);
	    if (administrador == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    // Asociar el administrador actual al evento
	    eventosComunidad.setAdministradore(administrador);

	    if (service.updateEventosComunidad(eventosComunidad) != null) {
	        return new ResponseEntity<>(HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	    }
	}

}
