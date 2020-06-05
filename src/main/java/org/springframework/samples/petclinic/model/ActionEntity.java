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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public class ActionEntity extends BaseEntity {

	@NotBlank
	@Column(name = "descripcion")
	private String	descripcion;

	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date	fecha;


	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew()).append("descripcion", this.descripcion).append("fecha", this.fecha).toString();
	}

}
