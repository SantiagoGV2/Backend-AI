package co.edu.ue.Project.AI.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.ue.Project.AI.model.Administradore;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.repository.IAdministradoreDao;

@Service
public class AdministradoreService implements IAdministradoreService{

	@Autowired
	IAdministradoreDao dao;
	@Override
	public Administradore registerAdministrador(Administradore administrador) {
		if(!administrador.equals(null))
			return dao.registerAdministrador(administrador);
		else
			return null;
	}

	@Override
	public Administradore updateAdministrador(Administradore administrador) {
		
		Administradore idDato = dao.searchById(administrador.getAdmId());
		
		if(idDato !=  null) {
			return dao.updateAdministrador(administrador);
		}else {
			return null;
		}
	}

	@Override
	public List<Administradore> allDatos() {
		
		return dao.allDatos();
	}

	@Override
	public Administradore searchById(int id) {
		
		return dao.searchById(id);
	}

	@Override
	public Administradore login(String email, String password) {
        
		Administradore admin = dao.login(email, password);
        
        if (admin != null) {
            return admin; 
        } else {
            return null;
        }
	}

	@Override
	public Administradore findByEmail(String email) {
		
		return dao.findByEmail(email);
	}

	@Override
	public Administradore findByNombre(String nombre) {
	
		return dao.findByNombre(nombre);
	}

	@Override
	public Administradore deleteAdministrador(int id) {
		
		Administradore adm = dao.searchById(id);
	    
	    if (adm != null) {
	        
	        dao.deleteAdministrador(id);
	        return adm;  
	    } else {
	        return null;  
	    }
	}

}
