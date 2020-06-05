
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
import org.springframework.samples.petclinic.web.validators.ExcursionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ExcursionValidatorTests {

	private Date fechaIn = java.sql.Date.valueOf(LocalDate.now().plusDays(5));
	private Date fechaFin = java.sql.Date.valueOf(LocalDate.now().plusDays(7));
	private String desc = "descripcion";
	private LocalTime horaIn = LocalTime.of(12, 0);
	private LocalTime horaFin = LocalTime.of(15, 0);
	private String titulo = "titulo";
	private Integer resis = 10;
	private Double ratio = 2.0;
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFexcoryBean = new LocalValidatorFactoryBean();
		localValidatorFexcoryBean.afterPropertiesSet();
		return localValidatorFexcoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(this.fechaIn);
		exc.setDescripcion(this.desc);
		exc.setHoraInicio(this.horaIn);
		exc.setHoraFin(this.horaFin);
		exc.setTitulo(this.titulo);
		exc.setFechaFin(fechaFin);
		exc.setFinalMode(true);
		exc.setNumeroResidencias(this.resis);
		exc.setRatioAceptacion(this.ratio);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Excursion>> constraintViolations = validator.validate(exc);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(null);
		exc.setDescripcion("");
		exc.setHoraInicio(null);
		exc.setHoraFin(null);
		exc.setTitulo("");
		exc.setFechaFin(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Excursion>> constraintViolations = validator.validate(exc);

		//Hay 7 campos incorrectos, y ratio y finalMode no es necesario hacerle el set
		Assertions.assertThat(constraintViolations.size()).isEqualTo(7);
	}

	@Test
	void noDebeValidarConFechaAnteriorActual() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(2)));
		exc.setDescripcion(this.desc);
		exc.setHoraInicio(this.horaIn);
		exc.setHoraFin(this.horaFin);
		exc.setTitulo(this.titulo);
		exc.setFechaFin(this.fechaFin);
		exc.setNumeroResidencias(this.resis);

		ExcursionValidator excursionValidator = new ExcursionValidator();
		
		Errors errors = new BeanPropertyBindingResult(exc, "");
		excursionValidator.validate(exc, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("fechaInicio")).isEqualTo(1);
	}

	@Test
	void noDebeValidarConFechaFinAnteriorAFechaInicio() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(this.fechaFin);
		exc.setDescripcion(this.desc);
		exc.setHoraInicio(this.horaIn);
		exc.setHoraFin(this.horaFin);
		exc.setTitulo(this.titulo);
		exc.setFechaFin(this.fechaIn);
		exc.setNumeroResidencias(this.resis);

		ExcursionValidator excursionValidator = new ExcursionValidator();

		Errors errors = new BeanPropertyBindingResult(exc, "");
		excursionValidator.validate(exc, errors);
		
		//La fechaFin debe ser posterior a la fechaInicio
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("fechaFin")).isEqualTo(1);
	}
	
	@Test
	void debeValidarConHoraFinAnteriorAHoraInicioDistintoDia() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(this.fechaIn);
		exc.setDescripcion(this.desc);
		exc.setHoraInicio(this.horaFin);
		exc.setHoraFin(this.horaIn);
		exc.setTitulo(this.titulo);
		exc.setFechaFin(this.fechaFin);
		exc.setNumeroResidencias(this.resis);

		ExcursionValidator excursionValidator = new ExcursionValidator();

		Errors errors = new BeanPropertyBindingResult(exc, "");
		excursionValidator.validate(exc, errors);
		
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}
	
	@Test
	void noDebeValidarConHoraFinAnteriorAHoraInicioMismoDia() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Excursion exc = new Excursion();
		exc.setFechaInicio(this.fechaIn);
		exc.setDescripcion(this.desc);
		exc.setHoraInicio(this.horaFin);
		exc.setHoraFin(this.horaIn);
		exc.setTitulo(this.titulo);
		exc.setFechaFin(this.fechaIn);
		exc.setNumeroResidencias(this.resis);

		ExcursionValidator excursionValidator = new ExcursionValidator();

		Errors errors = new BeanPropertyBindingResult(exc, "");
		excursionValidator.validate(exc, errors);
		
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("horaFin")).isEqualTo(1);
	}

}
