package co.edu.ue.Project.AI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.repository.IMensajeDao;

@Service
public class MensajeService implements IMensajeService{

	@Autowired
	IMensajeDao dao;
	@Override
	public Mensaje addMensajes(Mensaje mensaje) {
		
		return dao.addMensajes(mensaje);
	}

	@Override
	public Mensaje uppMensajes(Mensaje mensaje) {
		
		return dao.uppMensajes(mensaje);
	}

	@Override
	public List<Mensaje> getAllMensajes() {
		
		return dao.getAllMensajes();
	}

	@Override
	public Mensaje getIdMensaje(int msg_id) {
		
		return dao.getIdMensaje(msg_id);
	}

	@Override
	public boolean deleteMensaje(int msg_id) {
		
		return dao.deleteMensaje(msg_id);
	}
	
	@Override
    public List<Mensaje> obtenerMensajesEntreUsuarios(Usuario emisor, Usuario receptor) {
        return dao.findByEmisorAndReceptor(emisor, receptor);
    }

    @Override
    public List<Mensaje> obtenerMensajesDeUsuario(Usuario usuario) {
        return dao.findByEmisorOrReceptor(usuario, usuario);
    }

}
