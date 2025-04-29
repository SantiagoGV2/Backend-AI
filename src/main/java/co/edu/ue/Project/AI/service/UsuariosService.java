package co.edu.ue.Project.AI.service;

import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.repository.IEventos;
import co.edu.ue.Project.AI.repository.IUsuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService implements IUsuariosService {

    @Autowired
    IUsuarios dao;
    
    @Autowired
    IEventos eventoDao;

    @Override
    public Usuario agregarUsuarios(Usuario usuarios) {
        return dao.addUsuarios(usuarios);
    }

    @Override
    public Usuario actualizarUsuarios(Usuario usuarios) {
       return dao.uppUsuarios(usuarios);
    }

    @Override
    public List<Usuario> todasUsuarios() {
        return dao.getAllUsuarios();
    }

    @Override
    public Usuario buscarIdUsuarios(int usu_id) {
         return dao.getIdUsuarios(usu_id);
    }

    @Override
    public boolean bajaUsuarios(int usu_id) {
         return dao.deleteUsuarios(usu_id);
    }
    
    @Override
	public Usuario login(String email, String password) {
	
		Usuario admin = dao.login(email, password);
        
        if (admin != null) {
            return admin; 
        } else {
            return null;
        }
	}
    
    @Override
	public Usuario findByEmail(String email) {
		return dao.findByEmail(email);
	}

	@Override
	public Usuario findByNombre(String nombre) {
		
		return dao.findByNombre(nombre);
	}
	
	@Override
	public boolean borrarHistorialEventosUsuario(int usu_id) {
	    try {
	    	eventoDao.deleteByUsuarioId(usu_id);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}