package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;


public interface IEventosComunidadJpa extends JpaRepository<EventosComunidad, Integer>{

	EventosComunidad findByEveComuTitulo(String eveComuTitulo);
	List<Evento> findByadministradore_AdmId(int administradorId);
	List<EventosComunidad> findByAdministradore_AdmId(int administradorId); 

}
