package co.edu.ue.Project.AI.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the mensajes database table.
 * 
 */
@Entity
@Table(name="mensajes")
@NamedQuery(name="Mensaje.findAll", query="SELECT m FROM Mensaje m")
public class Mensaje implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="msg_id")
	private int msgId;

	@Lob
	@Column(name="msg_contenido")
	private String msgContenido;

	@Column(name="msg_fecha_envio")
	private Timestamp msgFechaEnvio;

	@ManyToOne
	@JoinColumn(name="usu_id_emisor")
	@JsonBackReference("usuario-mensajes-emisor")
	private Usuario emisor;

	@ManyToOne
	@JoinColumn(name="usu_id_receptor")
	@JsonBackReference("usuario-mensajes-receptor")
	private Usuario receptor;

	public Mensaje() {
	}

	public int getMsgId() {
		return this.msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public String getMsgContenido() {
		return this.msgContenido;
	}

	public void setMsgContenido(String msgContenido) {
		this.msgContenido = msgContenido;
	}

	public Timestamp getMsgFechaEnvio() {
		return this.msgFechaEnvio;
	}

	public void setMsgFechaEnvio(Timestamp msgFechaEnvio) {
		this.msgFechaEnvio = msgFechaEnvio;
	}

	public Usuario getEmisor() {
	    return this.emisor;
	}

	public void setEmisor(Usuario emisor) {
	    this.emisor = emisor;
	}

	public Usuario getReceptor() {
	    return this.receptor;
	}

	public void setReceptor(Usuario receptor) {
	    this.receptor = receptor;
	}


	

}