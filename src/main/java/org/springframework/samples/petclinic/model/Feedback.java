package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "excursion_id")
	private Excursion excursion;
	
	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia residencia;
	
	@Column(name = "descripcion")
	@NotBlank
	private String	descripcion;
	
	@Column(name = "valoracion")
	@NotNull
	@Range(min = 0, max = 5)
	private Integer	valoracion;
	
	@Column(name = "descartaFeedback")
	private boolean descartaFeedback;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getValoracion() {
		return valoracion;
	}

	public void setValoracion(Integer valoracion) {
		this.valoracion = valoracion;
	}

	public boolean isDescartaFeedback() {
		return descartaFeedback;
	}

	public void setDescartaFeedback(boolean descartaFeedback) {
		this.descartaFeedback = descartaFeedback;
	}

	@Override
	public String toString() {
		return "Feedback [excursion=" + excursion + ", residencia=" + residencia + ", descripcion=" + descripcion
				+ ", valoracion=" + valoracion + "]";
	}
	
}
