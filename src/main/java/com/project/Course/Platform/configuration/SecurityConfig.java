package com.project.Course.Platform.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.Course.Platform.component.JwtFilter;
import com.project.Course.Platform.service.CustomUserDetailsService;

@Configuration 
@EnableMethodSecurity 
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired 
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean 
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    {
        
        http.authorizeHttpRequests(auth->auth.requestMatchers("/api/signup","/api/login").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated());
        http.cors(cors->cors.configurationSource(configurationSource()));
        http.csrf(csrf->csrf.disable());
        http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.exceptionHandling(exception->exception.authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler)
        );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean 
    public DaoAuthenticationProvider daoAuthenticationProvider(CustomUserDetailsService customUser,PasswordEncoder passwordEncoder)
    {
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(customUser);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean 
    public AuthenticationManager authenticationManager( AuthenticationConfiguration configuration)
    {
       
        return configuration.getAuthenticationManager();

    }
    @Bean 
    public CorsConfigurationSource configurationSource()
    {
        CorsConfiguration configuration=new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
