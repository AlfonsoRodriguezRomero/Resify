
package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll()
			.antMatchers(HttpMethod.GET, "/", "/oups").permitAll()
			.antMatchers("/usuarios/new").anonymous()
			.antMatchers("/ancianos/new").anonymous()
			.antMatchers("/organizadores/new").anonymous()
			.antMatchers("/managers/new").anonymous()
			.antMatchers("/actividades/new").hasAnyAuthority("manager")
			.antMatchers("/actividades/{actividadId}").hasAnyAuthority("manager", "anciano")
			.antMatchers("/actividades").hasAnyAuthority("manager", "anciano")
			.antMatchers("/actividades/").hasAnyAuthority("manager", "anciano")
			.antMatchers("/actividades/**").hasAnyAuthority("manager")
			.antMatchers("/incidencias/**").hasAnyAuthority("manager")
			.antMatchers("/buenas-acciones/new").hasAnyAuthority("manager")
			.antMatchers("/buenas-acciones/**").hasAnyAuthority("manager")
			.antMatchers("/organizador/**").hasAnyAuthority("organizador")
			.antMatchers("/excursiones/{excursionId}/edit").hasAnyAuthority("organizador")
			.antMatchers("/excursiones/new").hasAnyAuthority("organizador")
			.antMatchers("/excursiones/{excursionId}").hasAnyAuthority("organizador", "manager", "anciano")
			.antMatchers("/excursiones/feedback").hasAnyAuthority("manager")
			.antMatchers("/excursiones/{excursionId}/peticiones-excursion/new").hasAnyAuthority("manager")
			.antMatchers("/excursiones/{excursionId}/feedbacks/new").hasAnyAuthority("manager")
			.antMatchers("/excursiones").hasAnyAuthority("organizador", "manager", "anciano")
			.antMatchers("/excursiones/**").hasAnyAuthority("organizador")
			.antMatchers("/peticiones-excursion").hasAnyAuthority("manager", "organizador")
			.antMatchers("/peticiones-excursion/**").hasAnyAuthority("organizador")
			.antMatchers("/feedbacks").hasAnyAuthority("manager", "organizador")
			.antMatchers("/feedbacks/**").hasAnyAuthority("organizador")
			.antMatchers("/residencias/new").hasAnyAuthority("manager")
			.antMatchers("/residencias").authenticated()
			.antMatchers("/residencias/top").hasAnyAuthority("anciano")
			.antMatchers("/residencias/ratio").hasAnyAuthority("organizador")
			.antMatchers("/residencias/no-participantes").hasAnyAuthority("organizador")
			.antMatchers("/residencias/{residenciaId}").hasAnyAuthority("organizador", "anciano", "manager")
			.antMatchers("/residencias/**").hasAnyAuthority("manager")
			.antMatchers("/inscripciones/new/**").hasAnyAuthority("anciano")
			.antMatchers("/inscripciones").hasAnyAuthority("anciano", "manager")
			.antMatchers("/inscripciones/**").hasAnyAuthority("anciano", "manager")
			.antMatchers("/quejas/new").hasAnyAuthority("anciano")
			.antMatchers("/quejas").hasAnyAuthority("manager")
			.antMatchers("/quejas/{quejaId}").hasAnyAuthority("manager")
			.antMatchers("/visitas-sanitarias").hasAnyAuthority("manager")
			.antMatchers("/visitas-sanitarias/**").hasAnyAuthority("manager")
			.antMatchers("/dashboard").hasAnyAuthority("admin")
			.antMatchers("/admin/**").hasAnyAuthority("admin")
			.antMatchers("/owners/**").hasAnyAuthority("owner", "admin")
//			.antMatchers("/ancianos/**").hasAnyAuthority("admin")
//			.antMatchers("/organizadores/**").hasAnyAuthority("admin")
//			.antMatchers("/managers/**").hasAnyAuthority("admin")
			.antMatchers("/vets/**").authenticated().anyRequest().denyAll().and().formLogin()
			/* .loginPage("/login") */
			.failureUrl("/login-error").and().logout().logoutSuccessUrl("/");
		// Configuración para que funcione la consola de administración 
		// de la BD H2 (deshabilitar las cabeceras de protección contra
		// ataques de tipo csrf y habilitar los framesets si su contenido
		// se sirve desde esta misma página.
		http.csrf().ignoringAntMatchers("/h2-console/**");
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource).usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
			.authoritiesByUsernameQuery("select username, authority " + "from authorities " + "where username = ?").passwordEncoder(this.passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		return encoder;
	}

}
