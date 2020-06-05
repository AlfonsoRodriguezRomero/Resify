package org.springframework.samples.petclinic.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "visitas_sanitarias")
public class VisitaSanitaria extends BaseEntity {

	@NotBlank
	@Column(name = "motivo")
	private String		motivo;

	@NotBlank
	@Column(name = "descripcion")
	private String		descripcion;

	@NotBlank
	@Column(name = "sanitario")
	private String		sanitario;

	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date		fecha;

	@NotNull
	@Column(name = "hora_inicio")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime	horaInicio;

	@NotNull
	@Column(name = "hora_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime	horaFin;

	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia	residencia;

	@ManyToOne
	@JoinColumn(name = "anciano_id")
	private Anciano		anciano;


	public Residencia getResidencia() {
		return this.residencia;
	}

	public void setResidencia(final Residencia residencia) {
		this.residencia = residencia;
	}

	public Anciano getAnciano() {
		return this.anciano;
	}

	public void setAnciano(final Anciano anciano) {
		this.anciano = anciano;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(final String motivo) {
		this.motivo = motivo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSanitario() {
		return this.sanitario;
	}

	public void setSanitario(final String sanitario) {
		this.sanitario = sanitario;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(final LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(final LocalTime horaFin) {
		this.horaFin = horaFin;
	}

}
