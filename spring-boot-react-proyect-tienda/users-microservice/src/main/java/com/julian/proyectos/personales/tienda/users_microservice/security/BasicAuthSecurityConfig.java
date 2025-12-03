package com.julian.proyectos.personales.tienda.users_microservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class BasicAuthSecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((auht) -> { auht
			.requestMatchers(HttpMethod.GET, "/").permitAll()
			.requestMatchers(HttpMethod.GET, "/{value}").permitAll()
			.anyRequest().permitAll();
		});
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		http.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
