
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
import org.springframework.samples.petclinic.web.validators.VisitaSanitariaValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class VisitaSanitariaValidatorTests {

	private String desc = "descripcion";
	private LocalTime horaIn = LocalTime.of(12, 0);
	private LocalTime horaFin = LocalTime.of(15, 0);
	private String motivo = "motivo";
	private String sanitario = "sanitario";
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		VisitaSanitaria vis = new VisitaSanitaria();
		vis.setDescripcion(this.desc);
		vis.setHoraInicio(this.horaIn);
		vis.setHoraFin(this.horaFin);
		vis.setMotivo(this.motivo);
		vis.setSanitario(this.sanitario);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<VisitaSanitaria>> constraintViolations = validator.validate(vis);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		VisitaSanitaria vis = new VisitaSanitaria();
		vis.setDescripcion("");
		vis.setHoraInicio(null);
		vis.setHoraFin(null);
		vis.setMotivo("");
		vis.setSanitario("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<VisitaSanitaria>> constraintViolations = validator.validate(vis);

		//Hay 5 campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(5);
	}

	@Test
	void noDebeValidarConHoraFinAnteriorAHoraInicio() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		VisitaSanitaria vis = new VisitaSanitaria();
		vis.setDescripcion(this.desc);
		vis.setHoraInicio(this.horaFin);
		vis.setHoraFin(this.horaIn);
		vis.setMotivo(this.motivo);
		vis.setSanitario(this.sanitario);

		VisitaSanitariaValidator visitaValidator = new VisitaSanitariaValidator();

		Errors errors = new BeanPropertyBindingResult(vis, "");
		visitaValidator.validate(vis, errors);
		
		//La horaFin debe ser posterior a la horaInicio
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("horaFin")).isEqualTo(1);
	}

}