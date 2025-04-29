package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.Administradore;


public interface IAdministradoreDao {

	Administradore registerAdministrador(Administradore administrador);
	Administradore updateAdministrador(Administradore administrador);
	List<Administradore> allDatos();
	Administradore searchById(int id);
	Administradore login(String email, String password);
	Administradore findByEmail(String email);
	Administradore findByNombre(String nombre);
	void deleteAdministrador(int id);
}
