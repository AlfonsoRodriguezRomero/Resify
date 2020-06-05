package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "ancianos")
public class Anciano extends Persona {

	@Column(name = "edad")
	@NotNull
	@Range(min = 65, max = 110)
	private Integer	edad;

	@Column(name = "cartaPresentacion")
	@NotBlank
	private String	cartaPresentacion;

	@Column(name = "tieneDependenciaGrave")
	@NotNull
	private boolean	tieneDependenciaGrave;

	//
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User	user;
	//


	public Integer getEdad() {
		return this.edad;
	}

	public void setEdad(final Integer edad) {
		this.edad = edad;
	}

	public String getCartaPresentacion() {
		return this.cartaPresentacion;
	}

	public void setCartaPresentacion(final String cartaPresentacion) {
		this.cartaPresentacion = cartaPresentacion;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public boolean getTieneDependenciaGrave() {
		return this.tieneDependenciaGrave;
	}

	public void setTieneDependenciaGrave(final boolean bool) {
		this.tieneDependenciaGrave = bool;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

			.append("id", this.getId()).append("new", this.isNew()).append("edad", this.edad).append("cartaPresentacion", this.cartaPresentacion).append("tieneDependenciaGrave", this.tieneDependenciaGrave).toString();
	}

}
