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
@Table(name = "organizadores")
public class Organizador extends Persona{

	@Column(name = "companya")
	@NotBlank
	private String companya;
	
	@Column(name = "sector")
	@NotBlank
	private String sector;
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

	public String getCompanya() {
		return companya;
	}

	public void setCompanya(String companya) {
		this.companya = companya;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.getId()).append("new", this.isNew()).append("companya", this.companya).append("sector", this.sector).toString();
	}

}
