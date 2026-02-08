package com.project.config;

import com.project.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(StudentRepository studentRepository,
            @Value("${spring.security.user.name:admin}") String adminUsername,
            @Value("${spring.security.user.password:admin}") String adminPassword,
            PasswordEncoder passwordEncoder) {
        return username -> {
            if (adminUsername.equalsIgnoreCase(username)) {
                UserDetails admin = User.withUsername(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles("ADMIN")
                        .build();
                return admin;
            }

            return studentRepository.findByEmailIgnoreCase(username)
                    .filter(student -> student.getPassword() != null && !student.getPassword().isBlank())
                    .map(student -> {
                        String role = student.getRole();
                        String normalizedRole = (role == null || role.isBlank()) ? "USER" : role;
                        if (normalizedRole.startsWith("ROLE_")) {
                            normalizedRole = normalizedRole.substring("ROLE_".length());
                        }
                        return User.withUsername(student.getEmail())
                                .password(student.getPassword())
                                .roles(normalizedRole)
                                .build();
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono uzytkownika: " + username));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                // .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // na czas statycznej integracji wyłączone.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/studenci/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/studenci/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/projekty/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/zadania/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/projekty/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projekty/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projekty/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/zadania/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/zadania/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/zadania/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
