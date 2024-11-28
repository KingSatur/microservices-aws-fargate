package com.jdcam.microservices.auth.service;

import com.jdcam.microservices.auth.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Component
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final WebClient.Builder webClient;

    public CustomUserDetailService(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDto userDto = this.webClient.build().get()
                    .uri("http://users-svc:8001/login",
                            uriBuilder -> uriBuilder.queryParam("email", username).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve().bodyToMono(UserDto.class).block();
            return new User(userDto.getEmail(), userDto.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            log.info("Error retrieving user: {}", username);
            throw new UsernameNotFoundException("User not found");
        }
    }
}
