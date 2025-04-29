package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the eventos_guardados database table.
 * 
 */
@Entity
@Table(name="eventos_guardados")
@NamedQuery(name="EventosGuardado.findAll", query="SELECT e FROM EventosGuardado e")
public class EventosGuardado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="eve_gua_id")
	private int eveGuaId;

	@CreationTimestamp
	@Column(name = "eve_gua_fecha", nullable = false, updatable = false)
	private Timestamp eveGuaFecha;

	//bi-directional many-to-one association to Evento
	@ManyToOne
	@JoinColumn(name="eve_id", nullable=true)
	@JsonBackReference(value = "eventoIA-guardar")
	private Evento evento;

	//bi-directional many-to-one association to EventosComunidad
	@ManyToOne
	@JoinColumn(name="eve_comu_id", nullable=true)
	@JsonBackReference(value = "evento-guardar")
	private EventosComunidad eventosComunidad;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usu_id", nullable=false)
	@JsonBackReference(value = "usuario-guardar")
	private Usuario usuario;

	public EventosGuardado() {
	}

	public int getEveGuaId() {
		return this.eveGuaId;
	}

	public void setEveGuaId(int eveGuaId) {
		this.eveGuaId = eveGuaId;
	}

	public Timestamp getEveGuaFecha() {
		return this.eveGuaFecha;
	}

	public void setEveGuaFecha(Timestamp eveGuaFecha) {
	    // Evitar sobrescribir si ya lo maneja @CreationTimestamp
	    if (this.eveGuaFecha == null && eveGuaFecha != null) {
	        this.eveGuaFecha = eveGuaFecha;
	    }
	}


	public Evento getEvento() {
		return this.evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
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