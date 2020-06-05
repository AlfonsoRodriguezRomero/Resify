package org.springframework.samples.petclinic.web.validators;

import java.util.Date;

import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BuenaAccionValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		BuenaAccion buenaAccion = (BuenaAccion) obj;
		Date hoy = new Date(System.currentTimeMillis());

		if (buenaAccion.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}
		if (buenaAccion.getFecha() == null || buenaAccion.getFecha().after(hoy)) {
			errors.rejectValue("fecha", "la fecha es requerida y debe estar en el pasado", "la fecha es requerida y debe estar en el pasado");
		}

		if (buenaAccion.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return BuenaAccion.class.isAssignableFrom(clazz);
	}

}
