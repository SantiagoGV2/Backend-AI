package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;

public interface IMensajeService {
	Mensaje addMensajes(Mensaje mensaje);
    Mensaje uppMensajes (Mensaje mensaje);
    
    List<Mensaje> getAllMensajes();
    Mensaje getIdMensaje(int msg_id);
    
    boolean deleteMensaje(int msg_id);
	List<Mensaje> obtenerMensajesDeUsuario(Usuario usuario);
	List<Mensaje> obtenerMensajesEntreUsuarios(Usuario emisor, Usuario receptor);
}
