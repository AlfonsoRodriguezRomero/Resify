package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "buenas_acciones")
public class BuenaAccion extends ActionEntity {

	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia residencia;
	
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "anciano_id") private Anciano anciano;
	 */
	@NotBlank
	private String titulo;
	
	public Residencia getResidencia() {
		return residencia;
	}

	public void setResidencia(Residencia residencia) {
		this.residencia = residencia;
	}
	
	/*
	 * public Anciano getAnciano() { return anciano; }
	 * 
	 * public void setAnciano(Anciano anciano) { this.anciano = anciano; }
	 */
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
