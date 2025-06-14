package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;
@Repository
public class EventosComunidadDao implements IEventosComunidadDao{

	@Autowired
	IEventosComunidadJpa jpa;
	@Override
	public EventosComunidad registerEventosComunidad(EventosComunidad eventosComunidad) {
		
		return jpa.save(eventosComunidad);
	}

	@Override
	public EventosComunidad updateEventosComunidad(EventosComunidad eventosComunidad) {
		
		return jpa.save(eventosComunidad);
	}

	@Override
	public List<EventosComunidad> allDatos() {
		
		return jpa.findAll();
	}

	@Override
	public EventosComunidad searchById(int id) {
		
		return jpa.findById(id).orElse(null);
	}

	@Override
	public EventosComunidad searchByTitulo(String titulo) {
		
		return jpa.findByEveComuTitulo(titulo);
	}

	@Override
	public void deleteEventosComunidad(int id) {
		
		jpa.deleteById(id);
	}

	@Override
	public List<Evento> findByAdmin_AdmId(int administradorId) {
		
		return jpa.findByadministradore_AdmId(administradorId);
	}

	@Override
	public List<EventosComunidad> findByAdministradore_AdmId(int admId) {
		
		return jpa.findByAdministradore_AdmId(admId);
	}

	@Override
	public List<EventosComunidad> filtrarEventos(String titulo, String categoria, String estado, String ubicacion) {
		
		return jpa.filtrarEventos(titulo, categoria, estado, ubicacion);
	}

}
