package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;

public interface IMensajeJpa extends JpaRepository<Mensaje, Integer>{

	List<Mensaje> findByEmisorOrReceptor(Usuario emisor, Usuario receptor);

    List<Mensaje> findByEmisorAndReceptor(Usuario emisor, Usuario receptor);
}
