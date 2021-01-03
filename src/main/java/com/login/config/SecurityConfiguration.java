package com.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.login.service.UserService;

@Configuration 
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private  UserService userService;
	
	//Metodo para codificar la password
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
		
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//---EJEMPLO BASICO DE CONFIGURACION 
		http
		.authorizeRequests() //autoriza en base a los siguientes parametros
		.antMatchers(
				"/registration**",
				"/js/**",
				"/css/**",
				"/img/**"
				)
		.permitAll() //<- permite las anteriores
		.anyRequest() //cualquier otra peticion
		.authenticated() //debe ser autenticada
		.and()
		.formLogin() //crea un login en esa ruta por defecto
		.loginPage("/login") //especificamos la ruta,aunque iba a ser ahi ¿?
		.permitAll() //permite las URLS anteriores
		.and()
		.logout() //permitimos hacer logout
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //url del logout
		.logoutSuccessUrl("/login?logout") //redireccionamos a login y mandamos una flag
		.permitAll();	//permite las urls anteriores	
	}
	
	
	
	
}
