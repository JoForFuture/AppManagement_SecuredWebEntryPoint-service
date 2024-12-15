package com.giogio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.giogio.DTO.UserDTO;
import com.giogio.services.UserService;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@SpringBootApplication
@EnableDiscoveryClient
public class SecuredWebEntryPoint {

	public static void main(String[] args) {
		SpringApplication.run(SecuredWebEntryPoint.class, args);
	}
	//UserService userService
	@Bean
	CommandLineRunner commandLineRunner() {
		return args->{
			
		};
	}

}
