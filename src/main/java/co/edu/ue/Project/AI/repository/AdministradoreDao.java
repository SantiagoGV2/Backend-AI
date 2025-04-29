package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.edu.ue.Project.AI.model.Administradore;
@Repository
public class AdministradoreDao implements IAdministradoreDao{

	@Autowired
	IAdministradoreJpa jpa;
	@Override
	public Administradore registerAdministrador(Administradore administrador) {
		
		return jpa.save(administrador);
	}

	@Override
	public Administradore updateAdministrador(Administradore administrador) {
	
		return jpa.save(administrador);
	}

	@Override
	public List<Administradore> allDatos() {
		
		return jpa.findAll();
	}

	@Override
	public Administradore searchById(int id) {
		
		return jpa.findById(id).orElse(null);
	}

	@Override
	public Administradore login(String email, String password) {
		
		return jpa.findByAdmEmailAndAdmPassword(email, password);
	}

	@Override
	public Administradore findByEmail(String email) {
		
		return jpa.findByAdmEmail(email);
	}

	@Override
	public Administradore findByNombre(String nombre) {
		
		return jpa.findByAdmNombre(nombre);
	}

	@Override
	public void deleteAdministrador(int id) {
		jpa.deleteById(id);
	}

}
