package com.candidate.mastcheshmi.onboarding.config;

import com.candidate.mastcheshmi.onboarding.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector)
        throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(
            sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers(new AntPathRequestMatcher("/client-api/*/register")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/client-api/*/logon")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll();
            auth.anyRequest().authenticated();
        }).build();
    }

    /**
     * To support multiple password encoders and ensure backward compatibility for changing the encoder, we can use the
     * DelegatingPasswordEncoder.While Argon2PasswordEncoder is a more secure option, for simplicity, I have used
     * BCryptPasswordEncoder here.
     *
     * @return A BCryptPasswordEncoder for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
