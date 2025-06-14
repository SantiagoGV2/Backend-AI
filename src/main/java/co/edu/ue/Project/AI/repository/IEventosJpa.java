package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import co.edu.ue.Project.AI.model.Evento;

public interface IEventosJpa extends JpaRepository<Evento, Integer> {
	
	@Query("SELECT COUNT(e) > 0 FROM Evento e WHERE e.eveTitulo = :titulo AND e.eveDescripcion = :descripcion")
	boolean existsByTituloYDescripcion(@Param("titulo") String titulo, @Param("descripcion") String descripcion);
	
	List<Evento> findByUsuario_UsuId(int usuarioId); 
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Evento e WHERE e.usuario.usuId = :usuId")
	void deleteByUsuarioId(@Param("usuId") int usuId);
	
}