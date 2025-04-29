package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.edu.ue.Project.AI.model.EventosCompartido;
@Repository
public class EventosCompartidoDao implements IEventosCompartidoDao {

	@Autowired
	IEventosCompartidoJpa jpa;
	@Override
	public EventosCompartido registerEventoCompartido(EventosCompartido eventosCompartido) {
		
		return jpa.save(eventosCompartido);
	}

	@Override
	public EventosCompartido updateEventoCompartido(EventosCompartido eventosCompartido) {
		
		return jpa.save(eventosCompartido);
	}

	@Override
	public List<EventosCompartido> allDatos() {
		
		return jpa.findAll();
	}

	@Override
	public EventosCompartido searchById(int id) {
		
		return jpa.findById(id).orElse(null);
	}

	@Override
	public void deleteEventosCompartido(int id) {
		
		jpa.deleteById(id);
	}

	
}
