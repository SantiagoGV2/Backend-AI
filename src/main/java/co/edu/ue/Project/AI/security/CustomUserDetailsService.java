package co.edu.ue.Project.AI.security;

import co.edu.ue.Project.AI.model.Administradore;
import co.edu.ue.Project.AI.model.Usuario;
import co.edu.ue.Project.AI.service.IAdministradoreService;
import co.edu.ue.Project.AI.service.IUsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuariosService usuariosService;

    @Autowired
    private IAdministradoreService administradoresService; // Asumo que tienes este servicio

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Primero, intentamos buscarlo como un Administrador
        Administradore admin = administradoresService.findByEmail(username);
        if (admin != null) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADM"));
            System.out.println(">>> ADMIN CARGADO: " + username + " CON PERMISOS: " + authorities);
            return new User(admin.getAdmEmail(), admin.getAdmPassword(), authorities);
        }

        // Si no es un admin, lo buscamos como un Usuario normal
        Usuario usuario = usuariosService.findByEmail(username);
        if (usuario != null) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            System.out.println(">>> USUARIO CARGADO: " + username + " CON PERMISOS: " + authorities);
            return new User(usuario.getUsuEmail(), usuario.getUsuPassword(), authorities);
        }

        // Si no se encuentra en ninguna de las dos tablas, lanzamos la excepción.
        throw new UsernameNotFoundException("Usuario o Administrador no encontrado con el email: " + username);
    }
}