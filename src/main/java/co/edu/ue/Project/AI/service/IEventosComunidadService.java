package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;

public interface IEventosComunidadService {

	EventosComunidad registerEventosComunidad(EventosComunidad eventosComunidad);
	EventosComunidad updateEventosComunidad(EventosComunidad eventosComunidad);
	List<EventosComunidad> allDatos();
	EventosComunidad searchById(int id);
	EventosComunidad searchByTitulo(String titulo);
	EventosComunidad deleteEventosComunidad(int id);
	List<Evento> obtenerEventosPorAdministrador(int admId);
	List<EventosComunidad> obtenerEventosComunidadPorAdministrador(int admId);
}
