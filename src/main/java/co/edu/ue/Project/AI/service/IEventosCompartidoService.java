package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.EventosCompartido;

public interface IEventosCompartidoService {

	EventosCompartido registerEventoCompartido(EventosCompartido eventosCompartido);
	EventosCompartido updateEventoCompartido(EventosCompartido eventosCompartido);
	List<EventosCompartido> allDatos();
	EventosCompartido searchById(int id);
	EventosCompartido deleteEventoCompartido(int id);
}
