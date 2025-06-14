package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

import co.edu.ue.Project.AI.model.EventosGuardado;

public interface IEventosGuardadoJpa extends JpaRepository<EventosGuardado, Integer>{
	List<EventosGuardado> findByUsuarioUsuId(int usuId);
	boolean existsByUsuarioUsuIdAndEventoEveId(int usuId, int eveId);
	boolean existsByUsuarioUsuIdAndEventosComunidadEveComuId(int usuId, int eveComuId);
	
	@Transactional
    void deleteAllByEvento_EveIdIn(List<Integer> eventoIds);
}
