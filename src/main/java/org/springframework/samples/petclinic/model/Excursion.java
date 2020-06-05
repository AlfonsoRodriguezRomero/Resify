package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "excursiones")
public class Excursion extends ActivityEntity {

	@PositiveOrZero
	@Column(name = "ratio")
	private double ratioAceptacion;

	@Positive
	@Column(name = "numeroResidencias")
	private int numeroResidencias;

	@NotNull
	@Column(name = "fecha_fin")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin;

	@Column(name = "final_mode")
	private boolean finalMode;

	@ManyToOne
	@JoinColumn(name = "organizador_id")
	private Organizador organizador;

	public Double getRatioAceptacion() {
		return this.ratioAceptacion;
	}

	public void setRatioAceptacion(final Double ratio) {
		this.ratioAceptacion = ratio;
	}

	public int getNumeroResidencias() {
		return this.numeroResidencias;
	}

	public void setNumeroResidencias(final int numeroResidencias) {
		this.numeroResidencias = numeroResidencias;
	}

	public Organizador getOrganizador() {
		return this.organizador;
	}

	public void setOrganizador(final Organizador organizador) {
		this.organizador = organizador;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(final Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

}
