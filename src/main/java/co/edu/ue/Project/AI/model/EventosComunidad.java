package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the eventos_comunidad database table.
 * 
 */
@Entity
@Table(name="eventos_comunidad")
@NamedQuery(name="EventosComunidad.findAll", query="SELECT e FROM EventosComunidad e")
public class EventosComunidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="eve_comu_id")
	private int eveComuId;

	@Column(name="eve_comu_categoria")
	private String eveComuCategoria;

	@Lob
	@Column(name="eve_comu_descripcion")
	private String eveComuDescripcion;

	@Column(name="eve_comu_enlace")
	private String eveComuEnlace;

	@Column(name="eve_comu_estado")
	private String eveComuEstado;

	@Column(name="eve_comu_fecha_creacion")
	private Timestamp eveComuFechaCreacion;

	@Column(name="eve_comu_fecha_fin")
	private Timestamp eveComuFechaFin;

	@Column(name="eve_comu_fecha_inicio")
	private Timestamp eveComuFechaInicio;

	@Column(name="eve_comu_titulo")
	private String eveComuTitulo;

	@Column(name="eve_comu_ubicacion")
	private String eveComuUbicacion;

	//bi-directional many-to-one association to EventosCompartido
	@OneToMany(mappedBy="eventosComunidad")
	@JsonManagedReference(value = "evento-compartido")
	private List<EventosCompartido> eventosCompartidos;

	//bi-directional many-to-one association to Administradore
	@ManyToOne
	@JoinColumn(name="adm_id")
	@JsonBackReference(value = "evento-adm")
	private Administradore administradore;

	//bi-directional many-to-one association to EventosGuardado
	@OneToMany(mappedBy="eventosComunidad")
	@JsonManagedReference(value = "evento-guardar")
	private List<EventosGuardado> eventosGuardados;

	//bi-directional many-to-one association to Notificacione
	@OneToMany(mappedBy="eventosComunidad")
	private List<Notificacione> notificaciones;

	public EventosComunidad() {
	}

	public int getEveComuId() {
		return this.eveComuId;
	}

	public void setEveComuId(int eveComuId) {
		this.eveComuId = eveComuId;
	}

	public String getEveComuCategoria() {
		return this.eveComuCategoria;
	}

	public void setEveComuCategoria(String eveComuCategoria) {
		this.eveComuCategoria = eveComuCategoria;
	}

	public String getEveComuDescripcion() {
		return this.eveComuDescripcion;
	}

	public void setEveComuDescripcion(String eveComuDescripcion) {
		this.eveComuDescripcion = eveComuDescripcion;
	}

	public String getEveComuEnlace() {
		return this.eveComuEnlace;
	}

	public void setEveComuEnlace(String eveComuEnlace) {
		this.eveComuEnlace = eveComuEnlace;
	}

	public String getEveComuEstado() {
		return this.eveComuEstado;
	}

	public void setEveComuEstado(String eveComuEstado) {
		this.eveComuEstado = eveComuEstado;
	}

	public Timestamp getEveComuFechaCreacion() {
		return this.eveComuFechaCreacion;
	}

	public void setEveComuFechaCreacion(Timestamp eveComuFechaCreacion) {
		this.eveComuFechaCreacion = eveComuFechaCreacion;
	}

	public Timestamp getEveComuFechaFin() {
		return this.eveComuFechaFin;
	}

	public void setEveComuFechaFin(Timestamp eveComuFechaFin) {
		this.eveComuFechaFin = eveComuFechaFin;
	}

	public Timestamp getEveComuFechaInicio() {
		return this.eveComuFechaInicio;
	}

	public void setEveComuFechaInicio(Timestamp eveComuFechaInicio) {
		this.eveComuFechaInicio = eveComuFechaInicio;
	}

	public String getEveComuTitulo() {
		return this.eveComuTitulo;
	}

	public void setEveComuTitulo(String eveComuTitulo) {
		this.eveComuTitulo = eveComuTitulo;
	}

	public String getEveComuUbicacion() {
		return this.eveComuUbicacion;
	}

	public void setEveComuUbicacion(String eveComuUbicacion) {
		this.eveComuUbicacion = eveComuUbicacion;
	}

	public List<EventosCompartido> getEventosCompartidos() {
		return this.eventosCompartidos;
	}

	public void setEventosCompartidos(List<EventosCompartido> eventosCompartidos) {
		this.eventosCompartidos = eventosCompartidos;
	}

	public EventosCompartido addEventosCompartido(EventosCompartido eventosCompartido) {
		getEventosCompartidos().add(eventosCompartido);
		eventosCompartido.setEventosComunidad(this);

		return eventosCompartido;
	}

	public EventosCompartido removeEventosCompartido(EventosCompartido eventosCompartido) {
		getEventosCompartidos().remove(eventosCompartido);
		eventosCompartido.setEventosComunidad(null);

		return eventosCompartido;
	}

	public Administradore getAdministradore() {
		return this.administradore;
	}

	public void setAdministradore(Administradore administradore) {
		this.administradore = administradore;
	}

	public List<EventosGuardado> getEventosGuardados() {
		return this.eventosGuardados;
	}

	public void setEventosGuardados(List<EventosGuardado> eventosGuardados) {
		this.eventosGuardados = eventosGuardados;
	}

	public EventosGuardado addEventosGuardado(EventosGuardado eventosGuardado) {
		getEventosGuardados().add(eventosGuardado);
		eventosGuardado.setEventosComunidad(this);

		return eventosGuardado;
	}

	public EventosGuardado removeEventosGuardado(EventosGuardado eventosGuardado) {
		getEventosGuardados().remove(eventosGuardado);
		eventosGuardado.setEventosComunidad(null);

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
		notificacione.setEventosComunidad(this);

		return notificacione;
	}

	public Notificacione removeNotificacione(Notificacione notificacione) {
		getNotificaciones().remove(notificacione);
		notificacione.setEventosComunidad(null);

		return notificacione;
	}

}