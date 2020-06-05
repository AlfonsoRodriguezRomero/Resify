package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.Queja;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class QuejaValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		Queja queja = (Queja) obj;

		if (queja.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}

		if (queja.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Queja.class.isAssignableFrom(clazz);
	}

}
