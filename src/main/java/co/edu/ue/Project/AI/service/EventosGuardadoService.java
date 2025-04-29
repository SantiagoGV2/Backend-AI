package co.edu.ue.Project.AI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.ue.Project.AI.dto.EventosGuardadoDTO;
import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;
import co.edu.ue.Project.AI.model.EventosGuardado;
import co.edu.ue.Project.AI.repository.IEventosGuardadoDao;

@Service
public class EventosGuardadoService implements IEventosGuardadoService {

	@Autowired
	IEventosGuardadoDao dao;
	
	@Override
	public EventosGuardado registerEventosGuardado(EventosGuardado eventosGuardado) {
		
		if(!eventosGuardado.equals(null))
			return dao.registerEventosGuardado(eventosGuardado);
		else
			return null;
	}

	@Override
	public EventosGuardado updateEventosGuardado(EventosGuardado eventosGuardado) {
		
		EventosGuardado idDato = dao.searchById(eventosGuardado.getEveGuaId());
		
		if(idDato !=  null) {
			return dao.updateEventosGuardado(eventosGuardado);
		}else {
			return null;
		}
	}

	@Override
	public List<EventosGuardado> allDatos() {
		
		return dao.allDatos();
	}

	@Override
	public EventosGuardado searchById(int id) {
		
		return dao.searchById(id);
	}

	@Override
	public EventosGuardado deleteEventosGuardado(int id) {
		
		EventosGuardado eventoG = dao.searchById(id);
	    
	    if (eventoG != null) {
	        
	        dao.deleteEventosGuardado(id);
	        return eventoG;  
	    } else {
	        return null;  
	    }
	}

	@Override
	public List<EventosGuardado> findByUsuarioUsuId(int usuId) {
		
		return dao.findByUsuarioUsuId(usuId);
	}

	@Override
	public List<EventosGuardadoDTO> findDetallesEventosGuardadosByUsuarioId(int usuId) {
	    List<EventosGuardado> eventosGuardados = dao.findByUsuarioUsuId(usuId);
	    List<EventosGuardadoDTO> dtos = new ArrayList<>();

	    for (EventosGuardado eg : eventosGuardados) {
	        EventosGuardadoDTO dto = new EventosGuardadoDTO();
	        dto.setEveGuaId(eg.getEveGuaId());
	        dto.setEveGuaFecha(eg.getEveGuaFecha());

	        // Datos del usuario
	        dto.setUsuId(eg.getUsuario().getUsuId());
	        dto.setUsuNombre(eg.getUsuario().getUsuNombre());
	        dto.setUsuCorreo(eg.getUsuario().getUsuEmail());

	        // Verificamos si se guardó un evento de comunidad
	        if (eg.getEventosComunidad() != null) {
	            EventosComunidad ec = eg.getEventosComunidad();
	            dto.setEventoComuId(ec.getEveComuId());
	            dto.setEventoComuTitulo(ec.getEveComuTitulo());
	            dto.setEventoComuDescripcion(ec.getEveComuDescripcion());
	            dto.setEventoComuUbicacion(ec.getEveComuUbicacion());
	            dto.setEventoComuFechaInicio(ec.getEveComuFechaInicio());
	            dto.setEventoComuFechaFin(ec.getEveComuFechaFin());
	            dto.setEventoCategoria(ec.getEveComuCategoria());
	            dto.setEventoComuEnlace(ec.getEveComuEnlace());
	        }

	        // Si no es de comunidad, es un evento general
	        if (eg.getEvento() != null) {
	            Evento e = eg.getEvento();
	            dto.setEventoId(e.getEveId());
	            dto.setEventoTitulo(e.getEveTitulo());
	            dto.setEventoDescripcion(e.getEveDescripcion());
	            dto.setEventoUbicacion(e.getEveUbicacion());
	            dto.setEventoFechaInicio(e.getEveFechaInicio());
	            dto.setEventoFechaFin(e.getEveFechaFin());
	            dto.setEventoCategoria(e.getEveCategoria());
	            dto.setEventoEnlace(e.getEveEnlace());
	        }

	        dtos.add(dto);
	    }

	    return dtos;
	}


}
