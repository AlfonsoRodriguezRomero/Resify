package org.springframework.samples.petclinic.web.validators;

import java.util.Date;

import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ActividadValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		Actividad actividad = (Actividad) obj;
		Date currentDate = new Date();

		if (actividad.getFechaInicio() != null && !actividad.getFechaInicio().after(currentDate)) {
			errors.rejectValue("fechaInicio", "debe ser fecha futura", "debe ser fecha futura");
		}

		if (actividad.getHoraFin() != null && actividad.getHoraInicio() != null
				&& actividad.getHoraInicio().isAfter(actividad.getHoraFin())) {
			errors.rejectValue("horaFin", "debe ser igual o posterior a la hora inicio",
					"debe ser igual o posterior a la hora inicio");
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Actividad.class.isAssignableFrom(clazz);
	}

}
