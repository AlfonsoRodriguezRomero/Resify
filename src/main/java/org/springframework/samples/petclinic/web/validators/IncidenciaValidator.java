package org.springframework.samples.petclinic.web.validators;

import java.util.Date;

import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class IncidenciaValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		Incidencia incidencia = (Incidencia) obj;
		Date hoy = new Date(System.currentTimeMillis());

		if (incidencia.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}
		if (incidencia.getFecha() == null || incidencia.getFecha().after(hoy)) {
			errors.rejectValue("fecha", "la fecha es requerida y debe estar en el pasado", "la fecha es requerida y debe estar en el pasado");
		}

		if (incidencia.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Incidencia.class.isAssignableFrom(clazz);
	}

}
