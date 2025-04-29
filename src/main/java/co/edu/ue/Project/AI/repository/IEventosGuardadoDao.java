package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.EventosGuardado;


public interface IEventosGuardadoDao {

	EventosGuardado registerEventosGuardado(EventosGuardado eventosGuardado);
	EventosGuardado updateEventosGuardado(EventosGuardado eventosGuardado);
	List<EventosGuardado> allDatos();
	EventosGuardado searchById(int id);
	void deleteEventosGuardado(int id);
	List<EventosGuardado> findByUsuarioUsuId(int usuId);
}
