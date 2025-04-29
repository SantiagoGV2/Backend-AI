package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the administradores database table.
 * 
 */
@Entity
@Table(name="administradores")
@NamedQuery(name="Administradore.findAll", query="SELECT a FROM Administradore a")
public class Administradore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="adm_id")
	private int admId;

	@Column(name="adm_email")
	private String admEmail;

	@Column(name="adm_nombre")
	private String admNombre;

	@Column(name="adm_password")
	private String admPassword;

	//bi-directional many-to-one association to EventosComunidad
	@OneToMany(mappedBy="administradore")
	@JsonManagedReference(value = "evento-adm")
	private List<EventosComunidad> eventosComunidads;

	public Administradore() {
	}

	public int getAdmId() {
		return this.admId;
	}

	public void setAdmId(int admId) {
		this.admId = admId;
	}

	public String getAdmEmail() {
		return this.admEmail;
	}

	public void setAdmEmail(String admEmail) {
		this.admEmail = admEmail;
	}

	public String getAdmNombre() {
		return this.admNombre;
	}

	public void setAdmNombre(String admNombre) {
		this.admNombre = admNombre;
	}

	public String getAdmPassword() {
		return this.admPassword;
	}

	public void setAdmPassword(String admPassword) {
		this.admPassword = admPassword;
	}

	public List<EventosComunidad> getEventosComunidads() {
		return this.eventosComunidads;
	}

	public void setEventosComunidads(List<EventosComunidad> eventosComunidads) {
		this.eventosComunidads = eventosComunidads;
	}

	public EventosComunidad addEventosComunidad(EventosComunidad eventosComunidad) {
		getEventosComunidads().add(eventosComunidad);
		eventosComunidad.setAdministradore(this);

		return eventosComunidad;
	}

	public EventosComunidad removeEventosComunidad(EventosComunidad eventosComunidad) {
		getEventosComunidads().remove(eventosComunidad);
		eventosComunidad.setAdministradore(null);

		return eventosComunidad;
	}

}