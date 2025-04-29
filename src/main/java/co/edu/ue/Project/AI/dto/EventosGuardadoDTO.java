package co.edu.ue.Project.AI.dto;

import java.sql.Timestamp;

public class EventosGuardadoDTO {

    private int eveGuaId;
    private Timestamp eveGuaFecha;

    // Datos del usuario que guardó el evento
    private int usuId;
    private String usuNombre;
    private String usuCorreo;

    // Datos del evento (si aplica)
    private Integer eventoId;
    private String eventoTitulo;
    private String eventoDescripcion;
    private String eventoUbicacion;
    private Timestamp eventoFechaInicio;
    private Timestamp eventoFechaFin;
    private String eventoCategoria;
    private String eventoEnlace;

    // Datos del evento comunidad (si aplica)
    private Integer eventoComuId;
    private String eventoComuTitulo;
    private String eventoComuDescripcion;
    private String eventoComuUbicacion;
    private Timestamp eventoComuFechaInicio;
    private Timestamp eventoComuFechaFin;
    private String eventoComuCategoria;
    private String eventoComuEnlace;

    public EventosGuardadoDTO() {}

    // Getters y setters

    public int getEveGuaId() {
        return eveGuaId;
    }

    public void setEveGuaId(int eveGuaId) {
        this.eveGuaId = eveGuaId;
    }

    public Timestamp getEveGuaFecha() {
        return eveGuaFecha;
    }

    public void setEveGuaFecha(Timestamp eveGuaFecha) {
        this.eveGuaFecha = eveGuaFecha;
    }

    public int getUsuId() {
        return usuId;
    }

    public void setUsuId(int usuId) {
        this.usuId = usuId;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuCorreo() {
        return usuCorreo;
    }

    public void setUsuCorreo(String usuCorreo) {
        this.usuCorreo = usuCorreo;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public void setEventoTitulo(String eventoTitulo) {
        this.eventoTitulo = eventoTitulo;
    }

    public String getEventoDescripcion() {
        return eventoDescripcion;
    }

    public void setEventoDescripcion(String eventoDescripcion) {
        this.eventoDescripcion = eventoDescripcion;
    }

    public String getEventoUbicacion() {
        return eventoUbicacion;
    }

    public void setEventoUbicacion(String eventoUbicacion) {
        this.eventoUbicacion = eventoUbicacion;
    }

    public Timestamp getEventoFechaInicio() {
        return eventoFechaInicio;
    }

    public void setEventoFechaInicio(Timestamp eventoFechaInicio) {
        this.eventoFechaInicio = eventoFechaInicio;
    }

    public Timestamp getEventoFechaFin() {
        return eventoFechaFin;
    }

    public void setEventoFechaFin(Timestamp eventoFechaFin) {
        this.eventoFechaFin = eventoFechaFin;
    }

    public String getEventoCategoria() {
		return eventoCategoria;
	}

	public void setEventoCategoria(String eventoCategoria) {
		this.eventoCategoria = eventoCategoria;
	}

	public String getEventoEnlace() {
		return eventoEnlace;
	}

	public void setEventoEnlace(String eventoEnlace) {
		this.eventoEnlace = eventoEnlace;
	}

	public String getEventoComuCategoria() {
		return eventoComuCategoria;
	}

	public void setEventoComuCategoria(String eventoComuCategoria) {
		this.eventoComuCategoria = eventoComuCategoria;
	}

	public String getEventoComuEnlace() {
		return eventoComuEnlace;
	}

	public void setEventoComuEnlace(String eventoComuEnlace) {
		this.eventoComuEnlace = eventoComuEnlace;
	}

	public Integer getEventoComuId() {
        return eventoComuId;
    }

    public void setEventoComuId(Integer eventoComuId) {
        this.eventoComuId = eventoComuId;
    }

    public String getEventoComuTitulo() {
        return eventoComuTitulo;
    }

    public void setEventoComuTitulo(String eventoComuTitulo) {
        this.eventoComuTitulo = eventoComuTitulo;
    }

    public String getEventoComuDescripcion() {
        return eventoComuDescripcion;
    }

    public void setEventoComuDescripcion(String eventoComuDescripcion) {
        this.eventoComuDescripcion = eventoComuDescripcion;
    }

    public String getEventoComuUbicacion() {
        return eventoComuUbicacion;
    }

    public void setEventoComuUbicacion(String eventoComuUbicacion) {
        this.eventoComuUbicacion = eventoComuUbicacion;
    }

    public Timestamp getEventoComuFechaInicio() {
        return eventoComuFechaInicio;
    }

    public void setEventoComuFechaInicio(Timestamp eventoComuFechaInicio) {
        this.eventoComuFechaInicio = eventoComuFechaInicio;
    }

    public Timestamp getEventoComuFechaFin() {
        return eventoComuFechaFin;
    }

    public void setEventoComuFechaFin(Timestamp eventoComuFechaFin) {
        this.eventoComuFechaFin = eventoComuFechaFin;
    }
}
