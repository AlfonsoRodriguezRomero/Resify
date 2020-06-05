package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PeticionExcursionValidator implements Validator {


	@Override
	public void validate(final Object obj, final Errors errors) {
		PeticionExcursion peticionExcursion = (PeticionExcursion) obj;

		if (peticionExcursion.getDeclaracion().isEmpty()) {
			errors.rejectValue("declaracion", "requerido", "requerido");
		}

		if (peticionExcursion.getEstado().equals("rechazada") && peticionExcursion.getJustificacion().isEmpty()) {
			errors.rejectValue("justificacion", "si rechaza la peticion, debe presentar una justificación", "si rechaza la peticion, debe presentar una justificación");
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return PeticionExcursion.class.isAssignableFrom(clazz);
	}

}
