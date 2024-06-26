package mdd_api.mdd_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import mdd_api.mdd_api.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	 private final AuthenticationProvider authenticationProvider;
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 
	 public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
			 AuthenticationProvider authenticationProvider) {
		 this.authenticationProvider = authenticationProvider;
		 this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	 
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {     

			return http
	                .csrf(csrf -> csrf.disable())
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .cors()
	                .and()
	                .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(
	                    		 "/swagger-ui/*", "/v3/api-docs", "/v3/api-docs/*",
	                    		"/auth/register", "/auth/login").permitAll() 
	                    .anyRequest().authenticated()) 
	                .exceptionHandling(e -> e
	                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
	                )
	                .authenticationProvider(authenticationProvider)
	                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)                
	                .build();        
	 }
	 
	 
	 
	
	
}
