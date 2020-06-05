
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.PeticionExcursionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class PeticionExcursionValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PeticionExcursion peticion = new PeticionExcursion();
		peticion.setDeclaracion("Declaracion test");
		peticion.setEstado("rechazada");
		peticion.setJustificacion("Justificacion test");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<PeticionExcursion>> constraintViolations = validator.validate(peticion);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		PeticionExcursion peticion = new PeticionExcursion();
		peticion.setDeclaracion("");
		peticion.setEstado("pendiente");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<PeticionExcursion>> constraintViolations = validator.validate(peticion);

		//Hay 1 campo incorrecto: declaracion
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<PeticionExcursion> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("declaracion");
	}

	@Test
	void noDebeValidarConEstadoRechazadoYSinJustificacion() {

		PeticionExcursion peticion = new PeticionExcursion();
		peticion.setDeclaracion("declaracion");
		peticion.setEstado("rechazada");
		peticion.setJustificacion("");

		PeticionExcursionValidator peticionExcursionValidator = new PeticionExcursionValidator();

		Errors errors = new BeanPropertyBindingResult(peticion, "");
		peticionExcursionValidator.validate(peticion, errors);

		//Si el estado es "rechazada" debe tener justificacion no vacía
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("justificacion")).isEqualTo(1);
	}


}
