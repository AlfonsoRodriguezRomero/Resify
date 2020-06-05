package org.springframework.samples.petclinic.web.validators;

import java.time.LocalTime;

import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ResidenciaValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		Residencia residencia = (Residencia) obj;

		final String FORMATO = "formato no válido";

		if (residencia.getNombre().isEmpty()) {
			errors.rejectValue("nombre", "requerido", "requerido");
		}

		if (residencia.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}

		if (residencia.getDireccion().isEmpty()) {
			errors.rejectValue("direccion", "requerido", "requerido");
		}

		if (residencia.getAforo().getClass() != Integer.class) {
			errors.rejectValue("aforo", FORMATO, FORMATO);
		} else if (residencia.getAforo() == null || residencia.getAforo() < 10) {
			errors.rejectValue("aforo", "debe ser superior o igual a 10", "debe ser superior o igual a 10");
		}else if (residencia.getAforo() > 1000) {
			errors.rejectValue("aforo", "no debe ser superior a 1000", "no debe ser superior a 1000");
		}

		if (!residencia.getMasInfo().isEmpty() && !residencia.getMasInfo().matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
			errors.rejectValue("masInfo", "debe ser una URL", "debe ser una URL");
		}

		if (residencia.getTelefono().isEmpty() || !residencia.getTelefono().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
			errors.rejectValue("telefono", "debe ser un teléfono. Ej: 954123456", "debe ser un teléfono. Ej: 954123456");
		}

		if (residencia.getCorreo().isEmpty() || !residencia.getCorreo().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
			errors.rejectValue("correo", "debe tener un formato de correo válido, por ejemplo 'correo@servidor.extension", "debe tener un formato de correo válido, por ejemplo 'correo@servidor.extension");
		}

		if (residencia.getEdadMaxima() == null) {
			errors.rejectValue("edadMaxima", "requerido", "requerido");
		} else if (residencia.getEdadMaxima().getClass() != Integer.class) {
			errors.rejectValue("edadMaxima", FORMATO, FORMATO);
		} else if (residencia.getEdadMaxima() < 65) {
			errors.rejectValue("edadMaxima", "la edad máxima debe ser igual o superior a 65 años", "la edad máxima debe ser igual o superior a 65 años");
		}else if (residencia.getEdadMaxima() > 110) {
			errors.rejectValue("edadMaxima", "la edad máxima no debe ser superior a 110 años", "la edad máxima no debe ser superior a 110 años");
		}

		if (residencia.getHoraApertura() == null) {
			errors.rejectValue("horaApertura", "requerido", "requerido");
		} else if (residencia.getHoraApertura().getClass() != LocalTime.class) {
			errors.rejectValue("horaApertura", FORMATO, FORMATO);
		}

		if (residencia.getHoraCierre() == null) {
			errors.rejectValue("horaCierre", "requerido", "requerido");
		} else if (residencia.getHoraCierre().getClass() != LocalTime.class) {
			errors.rejectValue("horaCierre", FORMATO, FORMATO);
		}

		if (residencia.getHoraApertura() != null && residencia.getHoraCierre() != null) {
			if (residencia.getHoraApertura().getClass() == LocalTime.class && residencia.getHoraCierre().getClass() == LocalTime.class) {
				if (residencia.getHoraApertura().equals(residencia.getHoraCierre()) || residencia.getHoraApertura().isAfter(residencia.getHoraCierre())) {
					errors.rejectValue("horaCierre", "debe ser posterior a la hora de apertura", "debe ser posterior a la hora de apertura");
				}
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Residencia.class.isAssignableFrom(clazz);
	}

}
