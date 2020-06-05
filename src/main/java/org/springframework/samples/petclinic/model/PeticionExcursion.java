package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "peticiones_excursion")
public class PeticionExcursion extends RequestEntity {

	@ManyToOne
	@JoinColumn(name = "excursion_id")
	private Excursion excursion;
	
	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia residencia;

	public Excursion getExcursion() {
		return excursion;
	}

	public void setExcursion(Excursion excursion) {
		this.excursion = excursion;
	}

	public Residencia getResidencia() {
		return residencia;
	}

	public void setResidencia(Residencia residencia) {
		this.residencia = residencia;
	}
	
}
