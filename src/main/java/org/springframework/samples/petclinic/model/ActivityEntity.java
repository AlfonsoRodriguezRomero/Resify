/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class ActivityEntity extends BaseEntity {

	@NotBlank
	@Column(name = "titulo")
	private String		titulo;

	@NotBlank
	@Column(name = "descripcion")
	private String		descripcion;

	@NotNull
	@Column(name = "fecha_inicio")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date		fechaInicio;

	@NotNull
	@Column(name = "hora_inicio")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime	horaInicio;

	@NotNull
	@Column(name = "hora_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime	horaFin;


	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

	public LocalTime getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(final LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(final LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(final Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew()).append("titulo", this.titulo).append("descripcion", this.descripcion).append("fecha_inicio", this.fechaInicio).append("hora_inicio", this.horaInicio)
			.append("hora_fin", this.horaFin).toString();
	}

}
