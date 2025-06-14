package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.Administradore;

public interface IAdministradoreService{

	Administradore registerAdministrador(Administradore administrador);
	Administradore updateAdministrador(Administradore administrador);
	List<Administradore> allDatos();
	Administradore searchById(int id);
	Administradore login(String email, String password);
	Administradore findByEmail(String email);
	Administradore findByNombre(String nombre);
	Administradore deleteAdministrador(int id);
}
