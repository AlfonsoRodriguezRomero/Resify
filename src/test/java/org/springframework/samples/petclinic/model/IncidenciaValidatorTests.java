
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.IncidenciaValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class IncidenciaValidatorTests {
	private Date hoy = new Date(System.currentTimeMillis() - 1);
	private Date futuro = java.sql.Date.valueOf(LocalDate.now().plusDays(10000));

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Incidencia inc = new Incidencia();
		inc.setDescripcion("desc");
		inc.setFecha(this.hoy);
		inc.setTitulo("tit");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Incidencia>> constraintViolations = validator.validate(inc);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConDescripcionVacia() {

		Incidencia inc = new Incidencia();
		inc.setDescripcion("");
		inc.setFecha(this.hoy);
		inc.setTitulo("tit");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Incidencia>> constraintViolations = validator.validate(inc);

		//Hay 1 campo incorrecto: descripcion
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Incidencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("descripcion");
	}
	
	@Test
	void noDebeValidarConTituloVacio() {

		Incidencia inc = new Incidencia();
		inc.setDescripcion("desc");
		inc.setFecha(this.hoy);
		inc.setTitulo("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Incidencia>> constraintViolations = validator.validate(inc);

		//Hay 1 campo incorrecto: titulo
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Incidencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
	}

	@Test
	void noDebeValidarConFechaFutura() {

		Incidencia inc = new Incidencia();
		inc.setDescripcion("desc");
		inc.setFecha(this.futuro);
		inc.setTitulo("tit");

		IncidenciaValidator incidenciaValidator = new IncidenciaValidator();

		Errors errors = new BeanPropertyBindingResult(inc, "");
		incidenciaValidator.validate(inc, errors);

		//No se puede registrar una incidencia usando fecha futura
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("fecha")).isEqualTo(1);
	}
	
	@Test
	void noDebeValidarConFechaNula() {

		Incidencia inc = new Incidencia();
		inc.setDescripcion("desc");
		inc.setFecha(null);
		inc.setTitulo("tit");

		IncidenciaValidator incidenciaValidator = new IncidenciaValidator();

		Errors errors = new BeanPropertyBindingResult(inc, "");
		incidenciaValidator.validate(inc, errors);

		//No se puede registrar una incidencia con fecha nula
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("fecha")).isEqualTo(1);
	}
}
