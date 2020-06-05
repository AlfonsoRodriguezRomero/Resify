package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class RequestEntity extends BaseEntity {

	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date	fecha;

	@Column(name = "estado")
	@NotBlank
	@Pattern(regexp = "^pendiente|aceptada|rechazada$")
	private String	estado;

	@Column(name = "declaracion")
	@NotBlank
	private String	declaracion;

	@Column(name = "justificacion")
	private String	justificacion;


	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(final String estado) {
		this.estado = estado;
	}

	public String getDeclaracion() {
		return this.declaracion;
	}

	public void setDeclaracion(final String declaracion) {
		this.declaracion = declaracion;
	}

	public String getJustificacion() {
		return this.justificacion;
	}

	public void setJustificacion(final String justificacion) {
		this.justificacion = justificacion;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew()).append("fecha", this.fecha).append("estado", this.estado).append("declaracion", this.declaracion).append("justificacion", this.justificacion).toString();
	}

}
