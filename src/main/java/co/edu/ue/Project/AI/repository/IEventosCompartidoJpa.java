package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

import co.edu.ue.Project.AI.model.EventosCompartido;

public interface IEventosCompartidoJpa extends JpaRepository<EventosCompartido, Integer>{
	@Transactional
    void deleteAllByEvento_EveIdIn(List<Integer> eventoIds);
}
