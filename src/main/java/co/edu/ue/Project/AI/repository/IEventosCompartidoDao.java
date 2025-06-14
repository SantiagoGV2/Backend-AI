package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.EventosCompartido;



public interface IEventosCompartidoDao {
	
	EventosCompartido registerEventoCompartido(EventosCompartido eventosCompartido);
	EventosCompartido updateEventoCompartido(EventosCompartido eventosCompartido);
	List<EventosCompartido> allDatos();
	EventosCompartido searchById(int id);
	void deleteEventosCompartido(int id);
	void deleteByEventoIdIn(List<Integer> eventoIds);
}
