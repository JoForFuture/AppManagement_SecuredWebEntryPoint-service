package com.giogio;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.giogio.DTO.UserDTO;
import com.giogio.services.UserService;

@SpringBootApplication
@EnableDiscoveryClient
public class SecuredWebEntryPoint {

	public static void main(String[] args) {
		SpringApplication.run(SecuredWebEntryPoint.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(UserService userService) {
		return args->{
			System.out.println("test-start");
			
			UserDTO user_1=UserDTO
					.builder()
					.nameDTO("First-name")
					.surnameDTO("First-surname")
					.build();
			
			String user_1_email="first@email.com";
			String user_2_email="second@email.com";
			
			Long user_1_id=1l;
			Long user_2_id=2l;

			
			userService.addUserIfNotPresent(
					 user_1 ,
					 user_1_email
					);
			try {
				Stream.of(userService.getUserByEmailOrId(user_1_email) )
				.forEach(System.out::println);
//				userService.getUserByEmail(user_1_email)
			}catch(NoSuchElementException e) {
				System.err.println("---NoSuchElementException---");
			}
		
			userService.getAllUsers().forEach(System.out::println);
			
//			System.err.println( userService.getUserById(5l).equals(userService.getUserById(4l)));


  
			  
		}
		 ;
	}

}
