package com.jdcam.microservices.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/**", "/authorized", "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/{id}")
                .hasAnyAuthority("SCOPE_read", "SCOPE_write")
                .antMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.PUT, "/{id}").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/oauth2/authorization/users-svc-client"))
                .csrf().disable()
                .oauth2Client(Customizer.withDefaults()).csrf().disable()
                .oauth2ResourceServer().jwt();

        return http.build();

    }

}
