package co.edu.ue.Project.AI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ue.Project.AI.model.Administradore;


public interface IAdministradoreJpa extends JpaRepository<Administradore, Integer>{
	
	Administradore findByAdmEmailAndAdmPassword(String admEmail, String admPassword);
	Administradore findByAdmEmail(String admEmail);
	Administradore findByAdmNombre(String admNombre);

}
