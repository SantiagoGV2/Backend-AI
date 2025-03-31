package co.edu.ue.Project.AI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.Usuario;

public interface IUsuariosJpa extends JpaRepository<Usuario, Integer> {
	Usuario findByUsuEmailAndUsuPassword(String usuEmail, String usuPassword);
	Usuario findByUsuEmail(String usuEmail);
	Usuario findByUsuNombre(String usuNombre);
	
}