package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the notificaciones database table.
 * 
 */
@Entity
@Table(name="notificaciones")
@NamedQuery(name="Notificacione.findAll", query="SELECT n FROM Notificacione n")
public class Notificacione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="not_id")
	private int notId;

	@Lob
	@Column(name="not_contenido")
	private String notContenido;

	@Column(name="not_fecha_envio")
	private Timestamp notFechaEnvio;

	@Column(name="not_leida")
	private byte notLeida;

	@Column(name="not_tipo")
	private String notTipo;

	//bi-directional many-to-one association to EventosComunidad
	@ManyToOne
	@JoinColumn(name="eve_comu_id")
	private EventosComunidad eventosComunidad;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usu_id")
	@JsonBackReference(value = "usuario-notificacion")
	private Usuario usuario;

	public Notificacione() {
	}

	public int getNotId() {
		return this.notId;
	}

	public void setNotId(int notId) {
		this.notId = notId;
	}

	public String getNotContenido() {
		return this.notContenido;
	}

	public void setNotContenido(String notContenido) {
		this.notContenido = notContenido;
	}

	public Timestamp getNotFechaEnvio() {
		return this.notFechaEnvio;
	}

	public void setNotFechaEnvio(Timestamp notFechaEnvio) {
		this.notFechaEnvio = notFechaEnvio;
	}

	public byte getNotLeida() {
		return this.notLeida;
	}

	public void setNotLeida(byte notLeida) {
		this.notLeida = notLeida;
	}

	public String getNotTipo() {
		return this.notTipo;
	}

	public void setNotTipo(String notTipo) {
		this.notTipo = notTipo;
	}

	public EventosComunidad getEventosComunidad() {
		return this.eventosComunidad;
	}

	public void setEventosComunidad(EventosComunidad eventosComunidad) {
		this.eventosComunidad = eventosComunidad;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}