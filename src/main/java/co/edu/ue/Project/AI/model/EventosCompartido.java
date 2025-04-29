package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the eventos_compartidos database table.
 * 
 */
@Entity
@Table(name="eventos_compartidos")
@NamedQuery(name="EventosCompartido.findAll", query="SELECT e FROM EventosCompartido e")
public class EventosCompartido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="eve_comp_id")
	private int eveCompId;

	@Column(name="eve_comp_fecha")
	private Timestamp eveCompFecha;

	@Column(name="eve_comp_medio")
	private String eveCompMedio;

	//bi-directional many-to-one association to Evento
	@ManyToOne
	@JoinColumn(name="eve_id")
	@JsonBackReference(value = "eventoIA-compartir")
	private Evento evento;

	//bi-directional many-to-one association to EventosComunidad
	@ManyToOne
	@JoinColumn(name="eve_comu_id")
	@JsonBackReference(value = "evento-compartido")
	private EventosComunidad eventosComunidad;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usu_id")
	@JsonBackReference(value = "usuario-compartir")
	private Usuario usuario;

	public EventosCompartido() {
	}

	public int getEveCompId() {
		return this.eveCompId;
	}

	public void setEveCompId(int eveCompId) {
		this.eveCompId = eveCompId;
	}

	public Timestamp getEveCompFecha() {
		return this.eveCompFecha;
	}

	public void setEveCompFecha(Timestamp eveCompFecha) {
		
		if(this.eveCompFecha == null && eveCompFecha != null) {
			this.eveCompFecha = eveCompFecha;
		}
	}

	public String getEveCompMedio() {
		return this.eveCompMedio;
	}

	public void setEveCompMedio(String eveCompMedio) {
		this.eveCompMedio = eveCompMedio;
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