package co.edu.ue.Project.AI.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import co.edu.ue.Project.AI.model.Usuario;

@Repository
public class UsuariosDao implements IUsuarios {

    @Autowired
    IUsuariosJpa jpa;

    @Override
    public Usuario addUsuarios(Usuario usuarios) {
        jpa.save(usuarios);
        return jpa.save(usuarios);   
      }
    

    @Override
    public Usuario uppUsuarios(Usuario usuarios) {
        return jpa.save(usuarios);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return jpa.findAll();
    }

    @Override
    public Usuario getIdUsuarios(int usu_id) {
        return jpa.findById(usu_id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + usu_id));
    }

    @Override
    public boolean deleteUsuarios(int usu_id) {
        if (jpa.existsById(usu_id)) {
            jpa.deleteById(usu_id);
            return true;
        }
        return false;
    }
    @Override
	public Usuario login(String email, String password) {
		
		return jpa.findByUsuEmailAndUsuPassword(email, password);	
	}
    
    @Override
	public Usuario findByEmail(String email) {
		
		return jpa.findByUsuEmail(email);
	}


	@Override
	public Usuario findByNombre(String nombre) {
		
		return jpa.findByUsuNombre(nombre);
	}
}