package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.Evento;



public interface IEventosService {
    
    Evento agregarEventos(Evento eventos);
    Evento actualizarEventos (Evento eventos);
    
    List<Evento> todasEventos();
    Evento buscarIdEventos(int eve_id);
    
    boolean bajaEventos(int eve_id);
    List<Evento> buscarEventosDesdeWeb(String consulta, int usuId);
	boolean existeEvento(Evento evento);
	List<Evento> obtenerEventosPorUsuario(int usuId);
	
	
}