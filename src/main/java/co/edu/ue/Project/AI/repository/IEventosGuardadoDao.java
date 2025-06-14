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
	boolean eventoGuardadoExiste(int usuId, int eveId, int eveComuId);
	boolean existsByUsuarioUsuIdAndEventoEveId(int usuId, int eveId);
	boolean existsByUsuarioUsuIdAndEventosComunidadEveComuId(int usuId, int eveComuId);
	void deleteByEventoIdIn(List<Integer> eventoIds);
}
