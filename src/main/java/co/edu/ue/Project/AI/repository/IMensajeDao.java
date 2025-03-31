package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.Mensaje;
import co.edu.ue.Project.AI.model.Usuario;

public interface IMensajeDao {
	Mensaje addMensajes(Mensaje mensaje);
    Mensaje uppMensajes (Mensaje mensaje);
    
    List<Mensaje> getAllMensajes();
    Mensaje getIdMensaje(int msg_id);
    
    boolean deleteMensaje(int msg_id);
	List<Mensaje> findByEmisorOrReceptor(Usuario usuario, Usuario usuario2);
	List<Mensaje> findByEmisorAndReceptor(Usuario emisor, Usuario receptor);
}
