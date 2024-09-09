package com.integracao.integracaomensageria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

	private final JwtFilter jwtFilter;
	
	public SecurityFilterConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeHttpRequests ->
            authorizeHttpRequests
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}
