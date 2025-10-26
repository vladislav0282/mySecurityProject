package com.example.mySecurityProject.config;

import com.example.mySecurityProject.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(PasswordEncoderConfig passwordEncoderConfig, UserService userService, JwtRequestFilter jwtRequestFilter) {
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
//                    .cors(cors -> cors.disable())
                    .cors()
                    .and()

                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers("/secured").authenticated()
//                            .requestMatchers("/info").authenticated()
                            .requestMatchers("/auth/login", "/auth/registration", "/auth/refresh").permitAll()
                            .requestMatchers("/auth/checkauth").authenticated()
//                           .requestMatchers("/users/getById/**").hasRole("USER")
                            .requestMatchers("/users/auth/**").hasRole("USER")
                                    .anyRequest().permitAll()
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

        //Базовые настройки
        @Bean
        public DaoAuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoderConfig.passwordEncoder());
            daoAuthenticationProvider.setUserDetailsService(userService);
            return daoAuthenticationProvider;
        }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // разрешаем ваш фронтенд
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }


