
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.ActividadValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ActividadValidatorTests {

	private Date fecha = java.sql.Date.valueOf(LocalDate.now().plusDays(5));
	private String desc = "descripcion";
	private LocalTime horaIn = LocalTime.of(12, 0);
	private LocalTime horaFin = LocalTime.of(15, 0);
	private String titulo = "titulo";
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actividad act = new Actividad();
		act.setFechaInicio(this.fecha);
		act.setDescripcion(this.desc);
		act.setHoraInicio(this.horaIn);
		act.setHoraFin(this.horaFin);
		act.setTitulo(this.titulo);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Actividad>> constraintViolations = validator.validate(act);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actividad act = new Actividad();
		act.setFechaInicio(null);
		act.setDescripcion("");
		act.setHoraInicio(null);
		act.setHoraFin(null);
		act.setTitulo("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Actividad>> constraintViolations = validator.validate(act);

		//Hay 5 campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(5);
	}

	@Test
	void noDebeValidarConFechaAnteriorActual() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actividad act = new Actividad();
		act.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(2)));
		act.setDescripcion(this.desc);
		act.setHoraInicio(this.horaIn);
		act.setHoraFin(this.horaFin);
		act.setTitulo(this.titulo);

		ActividadValidator actividadValidator = new ActividadValidator();
		
		Errors errors = new BeanPropertyBindingResult(act, "");
		actividadValidator.validate(act, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("fechaInicio")).isEqualTo(1);
	}


	@Test
	void noDebeValidarConHoraFinAnteriorAHoraInicio() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Actividad act = new Actividad();
		act.setFechaInicio(this.fecha);
		act.setDescripcion(this.desc);
		act.setHoraInicio(this.horaFin);
		act.setHoraFin(this.horaIn);
		act.setTitulo(this.titulo);

		ActividadValidator actividadValidator = new ActividadValidator();

		Errors errors = new BeanPropertyBindingResult(act, "");
		actividadValidator.validate(act, errors);
		
		//La horaFin debe ser posterior a la horaInicio
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("horaFin")).isEqualTo(1);
	}

}
