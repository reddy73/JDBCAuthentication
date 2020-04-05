package com.myapp.authenticateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class AuthenticateserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticateserviceApplication.class, args);
	}

}
