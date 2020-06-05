
package org.springframework.samples.petclinic.model;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.ResidenciaValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ResidenciaValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("");
		resi.setDescripcion("");
		resi.setDireccion("");
		resi.setEdadMaxima(null);
		resi.setHoraApertura(null);
		resi.setHoraCierre(null);
		resi.setNombre("");
		resi.setTelefono(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//Hay 8 campos incorrectos: correo, descripcion, direccion, edadMaxima, horaApertura,
		// horaCierre, nombre y teléfono
		Assertions.assertThat(constraintViolations.size()).isEqualTo(8);
	}

	@Test
	void noDebeValidarConAforoMenorA10() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(8);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//El aforo es incorrecto, ya que debe ser superior a 10
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Residencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("aforo");
	}

	@Test
	void noDebeValidarConMasInfoDistintoAURL() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");
		resi.setMasInfo("hola");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//Si hay un masInfo, debe ser una URL
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Residencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("masInfo");
	}

	@Test
	void noDebeValidarConTelefonoIncorrecto() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("hola");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//El teléfono debe tener formato correcto
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Residencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("telefono");
	}

	@Test
	void noDebeValidarConCorreoIncorrecto() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("hola");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//El correo debe tener formato correcto
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Residencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("correo");
	}

	@Test
	void noDebeValidarConEdadMaximaMenorA65() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(64);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(21, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Residencia>> constraintViolations = validator.validate(resi);

		//La edadMaxima debe ser igual o mayor a 65
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Residencia> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("edadMaxima");
	}

	@Test
	void noDebeValidarConHoraCierreAnteriorAHoraApertura() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Residencia resi = new Residencia();
		resi.setAforo(100);
		resi.setCorreo("corre@ejemplo.com");
		resi.setDescripcion("descrip");
		resi.setDireccion("direccion");
		resi.setEdadMaxima(70);
		resi.setHoraApertura(LocalTime.of(12, 0));
		resi.setHoraCierre(LocalTime.of(9, 0));
		resi.setNombre("Resi");
		resi.setTelefono("123456789");
		resi.setMasInfo("");

		ResidenciaValidator residenciaValidator = new ResidenciaValidator();

		Errors errors = new BeanPropertyBindingResult(resi, "");
		residenciaValidator.validate(resi, errors);

		//La horaCierre debe ser posterior a la horaApertura
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("horaCierre")).isEqualTo(1);
	}

}
