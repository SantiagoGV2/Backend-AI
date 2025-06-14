package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosGuardado;
@Repository
public class EventosGuardadoDao implements IEventosGuardadoDao{

	@Autowired
	IEventosGuardadoJpa jpa;
	@Override
	public EventosGuardado registerEventosGuardado(EventosGuardado eventosGuardado) {
		
		return jpa.save(eventosGuardado);
	}

	@Override
	public EventosGuardado updateEventosGuardado(EventosGuardado eventosGuardado) {
		
		return jpa.save(eventosGuardado);
	}

	@Override
	public List<EventosGuardado> allDatos() {
		
		return jpa.findAll();
	}

	@Override
	public EventosGuardado searchById(int id) {
		
		return jpa.findById(id).orElse(null);
	}

	@Override
	public void deleteEventosGuardado(int id) {
		
		jpa.deleteById(id);
	}

	@Override
	public List<EventosGuardado> findByUsuarioUsuId(int usuId) {
		
		return jpa.findByUsuarioUsuId(usuId);
	}

	@Override
	public boolean eventoGuardadoExiste(int usuId, int eveId, int eveComuId) {
	    if (eveId != 0) {
	        return jpa.existsByUsuarioUsuIdAndEventoEveId(usuId, eveId);
	    } else if (eveComuId != 0) {
	        return jpa.existsByUsuarioUsuIdAndEventosComunidadEveComuId(usuId, eveComuId);
	    }
	    return false;
	}

	@Override
	public boolean existsByUsuarioUsuIdAndEventoEveId(int usuId, int eveId) {
		
		return jpa.existsByUsuarioUsuIdAndEventoEveId(usuId, eveId);
	}

	@Override
	public boolean existsByUsuarioUsuIdAndEventosComunidadEveComuId(int usuId, int eveComuId) {
		
		return jpa.existsByUsuarioUsuIdAndEventosComunidadEveComuId(usuId, eveComuId);
	}

	@Override
	public void deleteByEventoIdIn(List<Integer> eventoIds) { // El nombre aquí puede seguir igual
	    jpa.deleteAllByEvento_EveIdIn(eventoIds); // Pero la llamada debe usar el nuevo nombre
	}

}
