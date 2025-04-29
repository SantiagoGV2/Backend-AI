package co.edu.ue.Project.AI.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;


/**
 * The persistent class for the suscripciones database table.
 * 
 */
@Entity
@Table(name="suscripciones")
@NamedQuery(name="Suscripcione.findAll", query="SELECT s FROM Suscripcione s")
public class Suscripcione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sub_id")
	private int subId;

	@Column(name="sub_activo")
	private byte subActivo;

	@Column(name="sub_tipo_notificacion")
	private String subTipoNotificacion;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usu_id")
	@JsonBackReference(value = "usuario-suscripcion")
	private Usuario usuario;

	public Suscripcione() {
	}

	public int getSubId() {
		return this.subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public byte getSubActivo() {
		return this.subActivo;
	}

	public void setSubActivo(byte subActivo) {
		this.subActivo = subActivo;
	}

	public String getSubTipoNotificacion() {
		return this.subTipoNotificacion;
	}

	public void setSubTipoNotificacion(String subTipoNotificacion) {
		this.subTipoNotificacion = subTipoNotificacion;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}