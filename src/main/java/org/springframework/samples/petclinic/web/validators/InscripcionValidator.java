package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InscripcionValidator implements Validator {

	public InscripcionService	inscripcionService;

	public Residencia			res;
	public Anciano				anc;


	public InscripcionValidator(final Residencia res, final Anciano anc) {
		this.anc = anc;
		this.res = res;
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		Inscripcion inscripcion = (Inscripcion) obj;

		if (inscripcion.getDeclaracion().isEmpty()) {
			errors.rejectValue("declaracion", "requerido", "requerido");
		}

		if (inscripcion.getEstado().equals("rechazada") && inscripcion.getJustificacion().isEmpty()) {
			errors.rejectValue("justificacion", "si rechaza la inscripcion, debe presentar una justificación", "si rechaza la inscripcion, debe presentar una justificación");
		}

		if (inscripcion.getEstado().equals("aceptada")) {
			if (this.res.getAceptaDependenciaGrave() == false && this.anc.getTieneDependenciaGrave() == true) {
				errors.rejectValue("estado", "su residencia no acepta personas con dependencia grave", "su residencia no acepta personas con dependencia grave");
			}
			if (this.res.getEdadMaxima() < this.anc.getEdad()) {
				errors.rejectValue("estado", "su residencia no acepta personas mayores de " + this.res.getEdadMaxima() + " años", "su residencia no acepta personas mayores de " + this.res.getEdadMaxima() + " años");
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Inscripcion.class.isAssignableFrom(clazz);
	}

}
