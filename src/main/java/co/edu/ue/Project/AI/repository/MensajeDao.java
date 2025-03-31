package co.edu.ue.Project.AI.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;

@Repository
public class MensajeDao implements IMensajeDao{
	
	@Autowired
	IMensajeJpa jpa;

	@Override
	public Mensaje addMensajes(Mensaje mensaje) {
		
		return jpa.save(mensaje);
	}

	@Override
	public Mensaje uppMensajes(Mensaje mensaje) {
		
		return jpa.save(mensaje);
	}

	@Override
	public List<Mensaje> getAllMensajes() {
		
		return jpa.findAll();
	}

	@Override
	public Mensaje getIdMensaje(int msg_id) {
		
		return jpa.findById(msg_id).orElse(null);
	}

	@Override
	public boolean deleteMensaje(int msg_id) {
		if (jpa.existsById(msg_id)) {
            jpa.deleteById(msg_id);
            return true;
        }
        return false;
	}

	@Override
	public List<Mensaje> findByEmisorOrReceptor(Usuario usuario, Usuario usuario2) {
		
		return jpa.findByEmisorOrReceptor(usuario, usuario2);
	}

	@Override
	public List<Mensaje> findByEmisorAndReceptor(Usuario emisor, Usuario receptor) {
		
		return jpa.findByEmisorAndReceptor(emisor, receptor);
	}

	
	
	
}
