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
	@JsonManagedReference(value = "usuario-evento")
	private List<Evento> eventos;

	//bi-directional many-to-one association to EventosCompartido
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference(value = "usuario-compartir")
	private List<EventosCompartido> eventosCompartidos;

	//bi-directional many-to-one association to EventosGuardado
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference(value = "usuario-guardar")
	private List<EventosGuardado> eventosGuardados;

	//bi-directional many-to-one association to Notificacione
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference(value = "usuario-notificacion")
	private List<Notificacione> notificaciones;

	//bi-directional many-to-one association to Suscripcione
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference(value = "usuario-suscripcion")
	private List<Suscripcione> suscripciones;

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

	public List<EventosCompartido> getEventosCompartidos() {
		return this.eventosCompartidos;
	}

	public void setEventosCompartidos(List<EventosCompartido> eventosCompartidos) {
		this.eventosCompartidos = eventosCompartidos;
	}

	public EventosCompartido addEventosCompartido(EventosCompartido eventosCompartido) {
		getEventosCompartidos().add(eventosCompartido);
		eventosCompartido.setUsuario(this);

		return eventosCompartido;
	}

	public EventosCompartido removeEventosCompartido(EventosCompartido eventosCompartido) {
		getEventosCompartidos().remove(eventosCompartido);
		eventosCompartido.setUsuario(null);

		return eventosCompartido;
	}

	public List<EventosGuardado> getEventosGuardados() {
		return this.eventosGuardados;
	}

	public void setEventosGuardados(List<EventosGuardado> eventosGuardados) {
		this.eventosGuardados = eventosGuardados;
	}

	public EventosGuardado addEventosGuardado(EventosGuardado eventosGuardado) {
		getEventosGuardados().add(eventosGuardado);
		eventosGuardado.setUsuario(this);

		return eventosGuardado;
	}

	public EventosGuardado removeEventosGuardado(EventosGuardado eventosGuardado) {
		getEventosGuardados().remove(eventosGuardado);
		eventosGuardado.setUsuario(null);

		return eventosGuardado;
	}

	public List<Notificacione> getNotificaciones() {
		return this.notificaciones;
	}

	public void setNotificaciones(List<Notificacione> notificaciones) {
		this.notificaciones = notificaciones;
	}

	public Notificacione addNotificacione(Notificacione notificacione) {
		getNotificaciones().add(notificacione);
		notificacione.setUsuario(this);

		return notificacione;
	}

	public Notificacione removeNotificacione(Notificacione notificacione) {
		getNotificaciones().remove(notificacione);
		notificacione.setUsuario(null);

		return notificacione;
	}

	public List<Suscripcione> getSuscripciones() {
		return this.suscripciones;
	}

	public void setSuscripciones(List<Suscripcione> suscripciones) {
		this.suscripciones = suscripciones;
	}

	public Suscripcione addSuscripcione(Suscripcione suscripcione) {
		getSuscripciones().add(suscripcione);
		suscripcione.setUsuario(this);

		return suscripcione;
	}

	public Suscripcione removeSuscripcione(Suscripcione suscripcione) {
		getSuscripciones().remove(suscripcione);
		suscripcione.setUsuario(null);

		return suscripcione;
	}

}