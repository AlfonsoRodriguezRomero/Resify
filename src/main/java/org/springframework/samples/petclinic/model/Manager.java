package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.core.style.ToStringCreator;


@Entity
@Table(name = "managers")
public class Manager extends Persona {

	@Column(name = "firma")
	@NotBlank
	private String firma;
	
	@Column(name = "declaracionResponsabilidad")
	@NotBlank
	private String declaracionResponsabilidad;
	//
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	//

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getDeclaracionResponsabilidad() {
		return declaracionResponsabilidad;
	}

	public void setDeclaracionResponsabilidad(String declaracionResponsabilidad) {
		this.declaracionResponsabilidad= declaracionResponsabilidad;
	}
	
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.getId()).append("new", this.isNew()).append("firma", this.firma)
				.append("declaracionResponsabilidad", this.declaracionResponsabilidad).toString();
	}

}
