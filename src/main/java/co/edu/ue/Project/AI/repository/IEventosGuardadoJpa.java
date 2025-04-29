package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.EventosGuardado;

public interface IEventosGuardadoJpa extends JpaRepository<EventosGuardado, Integer>{
	List<EventosGuardado> findByUsuarioUsuId(int usuId);
}
