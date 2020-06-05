package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public class Persona extends BaseEntity {

	@Column(name = "nombre")
	@NotBlank
	protected String	nombre;

	@Column(name = "apellidos")
	@NotBlank
	protected String	apellidos;


	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(final String apellidos) {
		this.apellidos = apellidos;
	}

}
