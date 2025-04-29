package co.edu.ue.Project.AI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.ue.Project.AI.model.EventosCompartido;
import co.edu.ue.Project.AI.repository.IEventosCompartidoDao;

@Service
public class EventosCompartidoService implements IEventosCompartidoService {

	@Autowired
	IEventosCompartidoDao dao;
	@Override
	public EventosCompartido registerEventoCompartido(EventosCompartido eventosCompartido) {
		if(!eventosCompartido.equals(null))
			return dao.registerEventoCompartido(eventosCompartido);
		else
			return null;
	}

	@Override
	public EventosCompartido updateEventoCompartido(EventosCompartido eventosCompartido) {
		
		EventosCompartido idDato = dao.searchById(eventosCompartido.getEveCompId());
		
		if(idDato !=  null) {
			return dao.updateEventoCompartido(eventosCompartido);
		}else {
			return null;
		}
	}

	@Override
	public List<EventosCompartido> allDatos() {
		
		return dao.allDatos();
	}

	@Override
	public EventosCompartido searchById(int id) {
		
		return dao.searchById(id);
	}

	@Override
	public EventosCompartido deleteEventoCompartido(int id) {
		
		EventosCompartido eventoC = dao.searchById(id);
	    
	    if (eventoC != null) {
	        
	        dao.deleteEventosCompartido(id);
	        return eventoC;  
	    } else {
	        return null;  
	    }
	}

}
