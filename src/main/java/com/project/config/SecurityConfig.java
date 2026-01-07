package com.project.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
@Bean
public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
 return httpSecurity
 .csrf(csrf -> csrf.disable())
//.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // na czas statycznej integracji wyłączone.
 .authorizeHttpRequests(auth -> auth
 .requestMatchers("/*.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
 .requestMatchers("/api/**").authenticated()
 .anyRequest().permitAll()
 )
 .httpBasic(Customizer.withDefaults())
 .build();
}
}