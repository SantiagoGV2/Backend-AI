package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.Evento;


public interface IEventos{
    
    Evento addEventos(Evento eventos);
    Evento uppEventos (Evento eventos);
    
    List<Evento> getAllEventos();
    Evento getIdEventos(int eve_id);
    
    boolean deleteEventos(int eve_id);
	boolean existsByTituloYDescripcion(String eveTitulo, String eveDescripcion);
	List<Evento> findEventosByFiltros(List<String> filtros);
	List<Evento> findByUsuario_UsuId(int usuarioId);
	void deleteByUsuarioId(int usu_id);
	
}