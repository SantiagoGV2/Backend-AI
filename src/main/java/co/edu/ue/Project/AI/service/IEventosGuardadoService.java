package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.dto.EventosGuardadoDTO;
import co.edu.ue.Project.AI.model.EventosGuardado;

public interface IEventosGuardadoService {

	EventosGuardado registerEventosGuardado(EventosGuardado eventosGuardado);
	EventosGuardado updateEventosGuardado(EventosGuardado eventosGuardado);
	List<EventosGuardado> allDatos();
	EventosGuardado searchById(int id);
	EventosGuardado deleteEventosGuardado(int id);
	List<EventosGuardado> findByUsuarioUsuId(int usuId);
	List<EventosGuardadoDTO> findDetallesEventosGuardadosByUsuarioId(int usuId);
}
