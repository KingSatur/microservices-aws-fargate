package com.jdcam.microservices.users;

import com.jdcam.microservices.users.entity.User;
import com.jdcam.microservices.users.services.UserService;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Slf4j
public class UsersApplication implements CommandLineRunner {

	private final UserService userService;

	public UsersApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<User> userOptional = this.userService.getByEmail("sample@mail.com");
		if (userOptional.isEmpty()) {
			User user = new User();
			user.setPassword("12345");
			user.setName("custom");
			user.setEmail("sample@mail.com");
			log.info("Creating user: {}", user);
			this.userService.create(user);
		}
	}
}
