package com.giogio.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<Long> addUser(){
		List<UserDTO> userEntityListFunctional=
				IntStream.range(1, 1001)
						 .mapToObj(i->
						 		UserDTO.builder()
						 				.nameDTO("name"+i)
						 				.surnameDTO("surname"+i)
						 				.ageDTO((int)(Math.random()*60))
						 				.emailDTO("email"+i+"@email.it")
						 				.build())
						 .collect(Collectors.toList());
		userEntityListFunctional
		.stream()
		.forEach(userService::addUserIfNotPresent);
		
		return new ResponseEntity<Long>(1l, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<UserDTO>> getAll(){
		try {
			return ResponseEntity.ok(userService.getAllUsers());
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity(null, HttpStatus.NO_CONTENT);
		}
	}

}
