package co.edu.ue.Project.AI.repository;

import java.util.List;

import co.edu.ue.Project.AI.model.Usuario;

public interface IUsuarios {
    
    Usuario addUsuarios(Usuario usuarios);
    Usuario uppUsuarios (Usuario usuarios);
    
    List<Usuario> getAllUsuarios();
    Usuario getIdUsuarios(int usu_id);
    Usuario login(String email, String password);
    boolean deleteUsuarios(int usu_id);
    Usuario findByEmail(String email);
    Usuario findByNombre(String nombre);
}