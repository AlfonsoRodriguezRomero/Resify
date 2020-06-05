
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.InscripcionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class InscripcionValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Inscripcion insc = new Inscripcion();
		insc.setDeclaracion("declaracion");
		insc.setEstado("pendiente");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscripcion>> constraintViolations = validator.validate(insc);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		Inscripcion insc = new Inscripcion();
		insc.setDeclaracion("");
		insc.setEstado("pendiente");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Inscripcion>> constraintViolations = validator.validate(insc);

		//Hay 1 campo incorrecto: declaracion
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Inscripcion> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("declaracion");
	}

	@Test
	void noDebeValidarConEstadoRechazadoYSinJustificacion() {

		Residencia resi = new Residencia();
		Anciano anciano = new Anciano();

		Inscripcion insc = new Inscripcion();
		insc.setDeclaracion("declaracion");
		insc.setEstado("rechazada");
		insc.setJustificacion("");

		InscripcionValidator inscripcionValidator = new InscripcionValidator(resi, anciano);

		Errors errors = new BeanPropertyBindingResult(insc, "");
		inscripcionValidator.validate(insc, errors);

		//Si el estado es "rechazada" debe tener justificacion no vacía
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("justificacion")).isEqualTo(1);
	}

	@Test
	void noDebeValidarConEstadoAceptadaSiAncianoConDependenciaYResidenciaNoAceptaDependencia() {

		Residencia resi = new Residencia();
		resi.setAceptaDependenciaGrave(false);
		resi.setEdadMaxima(90);
		Anciano anciano = new Anciano();
		anciano.setTieneDependenciaGrave(true);
		anciano.setEdad(70);

		Inscripcion insc = new Inscripcion();
		insc.setDeclaracion("declaracion");
		insc.setEstado("aceptada");

		InscripcionValidator inscripcionValidator = new InscripcionValidator(resi, anciano);

		Errors errors = new BeanPropertyBindingResult(insc, "");
		inscripcionValidator.validate(insc, errors);

		//Si el anciano tiene dependencia grave y la residencia no acepta personas
		// con dependencia grave, no debe tener estado "aceptada"
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("estado")).isEqualTo(1);
	}

	@Test
	void noDebeValidarConEstadoAceptadaSiAncianoConEdadMayorALaMaximaAceptadaEnResidencia() {

		Residencia resi = new Residencia();
		resi.setAceptaDependenciaGrave(true);
		resi.setEdadMaxima(90);
		Anciano anciano = new Anciano();
		anciano.setTieneDependenciaGrave(true);
		anciano.setEdad(91);

		Inscripcion insc = new Inscripcion();
		insc.setDeclaracion("declaracion");
		insc.setEstado("aceptada");

		InscripcionValidator inscripcionValidator = new InscripcionValidator(resi, anciano);

		Errors errors = new BeanPropertyBindingResult(insc, "");
		inscripcionValidator.validate(insc, errors);

		//Si el anciano tiene edad mayor a la edadMaxima de la residencia,
		// no debe tener estado "aceptada"
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("estado")).isEqualTo(1);
	}

}
