package com.saltyhana.saltyhanaserver.config;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.saltyhana.saltyhanaserver.filter.AuthFilter;
import com.saltyhana.saltyhanaserver.provider.JWTProvider;


@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${api_prefix}")
  private String apiPrefix;

  @Value("${jwt.access.interval}")
  private long accessInterval;

  @Value("${jwt.refresh.interval}")
  private long refreshInterval;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(httpSecurityCorsConfigurer ->
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(new AuthFilter(apiPrefix, jwtProvider()),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize -> {
          authorize
              .requestMatchers(apiPrefix + "/auth/unsubscribe").authenticated()
              .requestMatchers(apiPrefix + "/auth/**").permitAll()
              .requestMatchers(apiPrefix + "/password/**").permitAll()
              .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
              .requestMatchers(apiPrefix + "/**").authenticated()
              .anyRequest().permitAll();
        });
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JWTProvider jwtProvider() {
    return new JWTProvider(accessInterval, refreshInterval);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:${port}", "http://localhost:3000"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
