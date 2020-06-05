package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VisitaSanitariaValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		VisitaSanitaria visitaSanitaria = (VisitaSanitaria) obj;

		if (visitaSanitaria.getHoraFin() != null && visitaSanitaria.getHoraInicio() != null
				&& visitaSanitaria.getHoraInicio().isAfter(visitaSanitaria.getHoraFin())) {
			errors.rejectValue("horaFin", "debe ser igual o posterior a la hora inicio",
					"debe ser igual o posterior a la hora inicio");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return VisitaSanitaria.class.isAssignableFrom(clazz);
	}

}
