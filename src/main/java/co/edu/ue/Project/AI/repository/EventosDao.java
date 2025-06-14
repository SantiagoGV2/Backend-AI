package co.edu.ue.Project.AI.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import co.edu.ue.Project.AI.model.Evento;


@Repository
public class EventosDao implements IEventos {

    @Autowired
    IEventosJpa jpa;
    
    @Override
    public Evento addEventos(Evento eventos) {
       return jpa.save(eventos);
     
    }

    @Override
    public Evento uppEventos(Evento eventos) {
        return jpa.save(eventos);
    }

    @Override
    public List<Evento> getAllEventos() {
        return jpa.findAll();
    }

    @Override
    public Evento getIdEventos(int eve_id) {
        return jpa.findById(eve_id)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado con id " + eve_id));
    }

    @Override
    public boolean deleteEventos(int eve_id) {
        if (jpa.existsById(eve_id)) {
            jpa.deleteById(eve_id);
            return true;
        }
        return false;
    }

	public List<Evento> findByFuente(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsByTituloYDescripcion(String eveTitulo, String eveDescripcion) {
		return jpa.existsByTituloYDescripcion(eveTitulo, eveDescripcion);
	}

	@Override
	public List<Evento> findEventosByFiltros(List<String> filtros) {
	    if (filtros == null || filtros.isEmpty()) {
	        return jpa.findAll(); // Si no hay filtros, devuelve todos los eventos.
	    }

	    // Filtrar eventos en base a los filtros proporcionados
	    return jpa.findAll().stream()
	        .filter(evento -> filtros.stream()
	            .anyMatch(filtro -> evento.getEveTitulo().toLowerCase().contains(filtro) ||
	                                evento.getEveDescripcion().toLowerCase().contains(filtro) ||
	                                (evento.getEveUbicacion() != null && evento.getEveUbicacion().toLowerCase().contains(filtro))))
	        .collect(Collectors.toList());
	}
	
	
	@Override
	public List<Evento> findByUsuario_UsuId(int usuarioId) {
		
		return jpa.findByUsuario_UsuId(usuarioId);
	}

	@Override
	public void deleteByUsuarioId(int usu_id) {
	    jpa.deleteByUsuarioId(usu_id);
	}

	@Override
	public void deleteAllEventos(List<Evento> eventos) {
		System.out.println("Se eliminarán " + eventos.size() + " eventos del usuario.");
		jpa.deleteAll(eventos);
		
	}

}