package co.edu.ue.Project.AI.service;

import java.util.List;

import co.edu.ue.Project.AI.model.Usuario;

public interface IUsuariosService {
    
    Usuario agregarUsuarios(Usuario usuarios);
    Usuario actualizarUsuarios (Usuario usuarios);
    
    List<Usuario> todasUsuarios();
    Usuario buscarIdUsuarios(int usu_id);
    Usuario login(String email, String password);
    Usuario findByEmail(String email);
    Usuario findByNombre(String nombre);
    boolean bajaUsuarios(int usu_id);
}