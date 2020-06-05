package org.springframework.samples.petclinic.web.validators;

import java.util.Date;

import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExcursionValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		Excursion excursion = (Excursion) obj;
		Date currentDate = new Date();

		if (excursion.getFechaInicio() != null && !excursion.getFechaInicio().after(currentDate)) {
			errors.rejectValue("fechaInicio", "la fecha de inicio debe ser futura",
					"la fecha de inicio debe ser futura");
		}

		if (excursion.getFechaFin() != null && excursion.getFechaInicio() != null && excursion.getHoraInicio() != null
				&& excursion.getHoraFin() != null) {
			if (excursion.getFechaInicio().after(excursion.getFechaFin())) {
				errors.rejectValue("fechaFin", "debe ser igual o posterior a la fecha inicio",
						"debe ser igual o posterior a la fecha inicio");
			} else if (excursion.getFechaInicio().equals(excursion.getFechaFin())) {
				if (excursion.getHoraInicio().isAfter(excursion.getHoraFin())) {
					errors.rejectValue("horaFin", "debe ser igual o posterior a la hora inicio",
							"debe ser igual o posterior a la hora inicio");
				}
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Excursion.class.isAssignableFrom(clazz);
	}

}
