package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="usu_id")
	private int usuId;

	@Column(name="usu_email")
	private String usuEmail;

	@Column(name="usu_nombre")
	private String usuNombre;

	@Column(name="usu_password")
	private String usuPassword;

	//bi-directional many-to-one association to Evento
	@OneToMany(mappedBy="usuario")
	private List<Evento> eventos;

	@OneToMany(mappedBy="emisor")
	@JsonManagedReference(value = "usuario-mensajes-emisor")
	private List<Mensaje> mensajesEnviados;

	@OneToMany(mappedBy="receptor")
	@JsonManagedReference(value = "usuario-mensajes-receptor")
	private List<Mensaje> mensajesRecibidos;

	public Usuario() {
	}

	public int getUsuId() {
		return this.usuId;
	}

	public void setUsuId(int usuId) {
		this.usuId = usuId;
	}

	public String getUsuEmail() {
		return this.usuEmail;
	}

	public void setUsuEmail(String usuEmail) {
		this.usuEmail = usuEmail;
	}

	public String getUsuNombre() {
		return this.usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
	}

	public String getUsuPassword() {
		return this.usuPassword;
	}

	public void setUsuPassword(String usuPassword) {
		this.usuPassword = usuPassword;
	}

	public List<Evento> getEventos() {
		return this.eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public Evento addEvento(Evento evento) {
		getEventos().add(evento);
		evento.setUsuario(this);

		return evento;
	}

	public Evento removeEvento(Evento evento) {
		getEventos().remove(evento);
		evento.setUsuario(null);

		return evento;
	}

	public List<Mensaje> getMensajesEnviados() {
	    return this.mensajesEnviados;
	}

	public void setMensajesEnviados(List<Mensaje> mensajesEnviados) {
	    this.mensajesEnviados = mensajesEnviados;
	}

	public List<Mensaje> getMensajesRecibidos() {
	    return this.mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
	    this.mensajesRecibidos = mensajesRecibidos;
	}


	public Mensaje addMensajeEnviado(Mensaje mensaje) {
	    getMensajesEnviados().add(mensaje);
	    mensaje.setEmisor(this);
	    return mensaje;
	}

	public Mensaje removeMensajeEnviado(Mensaje mensaje) {
	    getMensajesEnviados().remove(mensaje);
	    mensaje.setEmisor(null);
	    return mensaje;
	}

	public Mensaje addMensajeRecibido(Mensaje mensaje) {
	    getMensajesRecibidos().add(mensaje);
	    mensaje.setReceptor(this);
	    return mensaje;
	}

	public Mensaje removeMensajeRecibido(Mensaje mensaje) {
	    getMensajesRecibidos().remove(mensaje);
	    mensaje.setReceptor(null);
	    return mensaje;
	}
}