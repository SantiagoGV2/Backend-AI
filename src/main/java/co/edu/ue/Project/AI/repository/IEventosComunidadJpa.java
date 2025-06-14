package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.Evento;
import co.edu.ue.Project.AI.model.EventosComunidad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IEventosComunidadJpa extends JpaRepository<EventosComunidad, Integer>{

	EventosComunidad findByEveComuTitulo(String eveComuTitulo);
	List<Evento> findByadministradore_AdmId(int administradorId);
	List<EventosComunidad> findByAdministradore_AdmId(int administradorId); 
	@Query("SELECT e FROM EventosComunidad e WHERE " +
	           "(:titulo IS NULL OR LOWER(e.eveComuTitulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
	           "(:categoria IS NULL OR LOWER(e.eveComuCategoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
	           "(:estado IS NULL OR LOWER(e.eveComuEstado) = LOWER(:estado)) AND " +
	           "(:ubicacion IS NULL OR LOWER(e.eveComuUbicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%')))")
	    List<EventosComunidad> filtrarEventos(
	        @Param("titulo") String titulo,
	        @Param("categoria") String categoria,
	        @Param("estado") String estado,
	        @Param("ubicacion") String ubicacion
	    );

}
