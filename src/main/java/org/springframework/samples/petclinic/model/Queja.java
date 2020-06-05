package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "quejas")
public class Queja extends ActionEntity{
	
	@Column(name = "anonimo")
	private boolean anonimo;
	
	@NotBlank
	@Column(name = "titulo")
	private String titulo;

	@ManyToOne
	@JoinColumn(name = "anciano_id")
	private Anciano anciano;

	
	public Boolean getAnonimo() {
		return anonimo;
	}

	public void setAnonimo(Boolean anonimo) {
		this.anonimo = anonimo;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Anciano getAnciano() {
		return anciano;
	}

	public void setAnciano(Anciano anciano) {
		this.anciano = anciano;
	}
	

}
