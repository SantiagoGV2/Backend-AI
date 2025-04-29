package co.edu.ue.Project.AI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;
import co.edu.ue.Project.AI.repository.IEventosComunidadDao;


@Service
public class EventosComunidadService implements IEventosComunidadService{

	@Autowired
	IEventosComunidadDao dao;
	@Override
	public EventosComunidad registerEventosComunidad(EventosComunidad eventosComunidad) {
		
		if(!eventosComunidad.equals(null))
			return dao.registerEventosComunidad(eventosComunidad);
		else
			return null;
	}

	@Override
	public EventosComunidad updateEventosComunidad(EventosComunidad eventosComunidad) {
		
		EventosComunidad idDato = dao.searchById(eventosComunidad.getEveComuId());
		
		if(idDato !=  null) {
			return dao.updateEventosComunidad(eventosComunidad);
		}else {
			return null;
		}
	}

	@Override
	public List<EventosComunidad> allDatos() {
		
		return dao.allDatos();
	}

	@Override
	public EventosComunidad searchById(int id) {
		
		return dao.searchById(id);
	}

	@Override
	public EventosComunidad searchByTitulo(String titulo) {
		
		return dao.searchByTitulo(titulo);
	}

	@Override
	public EventosComunidad deleteEventosComunidad(int id) {
		
		EventosComunidad eventoCo = dao.searchById(id);
	    
	    if (eventoCo != null) {
	        
	        dao.deleteEventosComunidad(id);
	        return eventoCo;  
	    } else {
	        return null;  
	    }
	}
	
	public List<Evento> obtenerEventosPorAdministrador(int administradorId) {
        return dao.findByAdmin_AdmId(administradorId);
    }

	@Override
	public List<EventosComunidad> obtenerEventosComunidadPorAdministrador(int admId) {
	    return dao.findByAdministradore_AdmId(admId);
	}


}
