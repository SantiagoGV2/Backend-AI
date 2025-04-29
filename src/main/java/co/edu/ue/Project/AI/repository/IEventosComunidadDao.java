package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;



public interface IEventosComunidadDao {

	EventosComunidad registerEventosComunidad(EventosComunidad eventosComunidad);
	EventosComunidad updateEventosComunidad(EventosComunidad eventosComunidad);
	List<EventosComunidad> allDatos();
	EventosComunidad searchById(int id);
	EventosComunidad searchByTitulo(String titulo);
	void deleteEventosComunidad(int id);
	List<Evento> findByAdmin_AdmId(int admId);
	List<EventosComunidad> findByAdministradore_AdmId(int admId);
}
