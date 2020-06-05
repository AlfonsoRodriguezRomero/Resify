package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inscripciones")
public class Inscripcion extends RequestEntity {

	@ManyToOne
	@JoinColumn(name = "anciano_id")
	private Anciano		anciano;

	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia	residencia;


	public Anciano getAnciano() {
		return this.anciano;
	}

	public void setAnciano(final Anciano anciano) {
		this.anciano = anciano;
	}

	public Residencia getResidencia() {
		return this.residencia;
	}

	public void setResidencia(final Residencia residencia) {
		this.residencia = residencia;
	}

}
