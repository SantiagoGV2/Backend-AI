package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the eventos database table.
 * 
 */
@Entity
@Table(name="eventos")
@NamedQuery(name="Evento.findAll", query="SELECT e FROM Evento e")
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="eve_id")
	private int eveId;

	@Lob
	@Column(name="eve_descripcion")
	private String eveDescripcion;

	@Column(name="eve_enlace")
	private String eveEnlace;

	@Column(name="eve_estado")
	private String eveEstado;

	@Column(name="eve_fecha_creacion")
	private Timestamp eveFechaCreacion;

	@Column(name="eve_fecha_fin")
	private Timestamp eveFechaFin;

	@Column(name="eve_fecha_inicio")
	private Timestamp eveFechaInicio;

	@Column(name="eve_fecha_modificacion")
	private Timestamp eveFechaModificacion;

	@Column(name="eve_titulo")
	private String eveTitulo;

	@Column(name="eve_ubicacion")
	private String eveUbicacion;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usu_id")
	@JsonBackReference
	private Usuario usuario;

	public Evento() {
	}

	public int getEveId() {
		return this.eveId;
	}

	public void setEveId(int eveId) {
		this.eveId = eveId;
	}

	public String getEveDescripcion() {
		return this.eveDescripcion;
	}

	public void setEveDescripcion(String eveDescripcion) {
		this.eveDescripcion = eveDescripcion;
	}

	public String getEveEnlace() {
		return this.eveEnlace;
	}

	public void setEveEnlace(String eveEnlace) {
		this.eveEnlace = eveEnlace;
	}

	public String getEveEstado() {
		return this.eveEstado;
	}

	public void setEveEstado(String eveEstado) {
		this.eveEstado = eveEstado;
	}

	public Timestamp getEveFechaCreacion() {
		return this.eveFechaCreacion;
	}

	public void setEveFechaCreacion(Timestamp eveFechaCreacion) {
		this.eveFechaCreacion = eveFechaCreacion;
	}

	public Timestamp getEveFechaFin() {
		return this.eveFechaFin;
	}

	public void setEveFechaFin(Timestamp eveFechaFin) {
		this.eveFechaFin = eveFechaFin;
	}

	public Timestamp getEveFechaInicio() {
		return this.eveFechaInicio;
	}

	public void setEveFechaInicio(Timestamp eveFechaInicio) {
		this.eveFechaInicio = eveFechaInicio;
	}

	public Timestamp getEveFechaModificacion() {
		return this.eveFechaModificacion;
	}

	public void setEveFechaModificacion(Timestamp eveFechaModificacion) {
		this.eveFechaModificacion = eveFechaModificacion;
	}

	public String getEveTitulo() {
		return this.eveTitulo;
	}

	public void setEveTitulo(String eveTitulo) {
		this.eveTitulo = eveTitulo;
	}

	public String getEveUbicacion() {
		return this.eveUbicacion;
	}

	public void setEveUbicacion(String eveUbicacion) {
		this.eveUbicacion = eveUbicacion;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public int getUsuid() {
	    return (usuario != null) ? usuario.getUsuId() : 0; // Retorna 0 si no hay usuario asociado
	}
	
	 public void setUsuid(int usuid) {
	        if (this.usuario == null) {
	            this.usuario = new Usuario();
	        }
	        this.usuario.setUsuId(usuid);
	    }
	

}