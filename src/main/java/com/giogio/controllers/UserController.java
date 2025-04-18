
package com.giogio.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giogio.DTO.UserDTO;
import com.giogio.services.UserService;
import com.giogio.services.utilities.SearchFilterTypeEnum;

import jakarta.validation.Validation;
import jakarta.validation.Validator;


@RestController
@RequestMapping("/user")
public class UserController {
	
	private UserService userService;
	
    private final Validator validator;

	
	public UserController(UserService userService) {
		this.userService=userService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		
	}
	
//	CREATE
	
	@PostMapping("/save")
	public ResponseEntity<Long> save(@Validated @RequestBody UserDTO userDTO){
		try {
			Long responseId=userService.addUserIfNotPresent(userDTO);
			return 
					new ResponseEntity<Long>(responseId,HttpStatus.CREATED);
		}catch(RuntimeException re) {
			return 
					new ResponseEntity<Long>(-1l,HttpStatus.NOT_ACCEPTABLE);
		}
	
		
	}
	
	
	@Deprecated
	@PostMapping("/addRandomUsers")
	public ResponseEntity<List<Long>> addUsers(@RequestParam (defaultValue="5") int amount){
		List<UserDTO> userEntityListFunctional=
				IntStream.range(1, amount+1)
						 .mapToObj(i->
						 		UserDTO.builder()
						 				.nameDTO("name"+i)
						 				.surnameDTO("surname"+i)
						 				.ageDTO((int)(Math.random()*60))
						 				.emailDTO("email"+i+"@email.it")
						 				.build())
						 .collect(Collectors.toList());
		
		List<Long> responseList=userEntityListFunctional
				.stream()
				.map(userService::addUserIfNotPresent)
				.toList();	
		
		return new ResponseEntity<List<Long>>(responseList, HttpStatus.OK);
	}
	
	
//	READ
	
	@GetMapping("/get")
	public ResponseEntity<List<UserDTO>> get(
			@RequestParam (required=false) Long id,
			@RequestParam (required=false) String email,
			@RequestParam (required=false) String name,
			@RequestParam (required=false) String surname,
			@RequestParam (required=false) Integer age,
			@RequestParam (required=false) String nameSurname

			){
		
		
		UserDTO request=UserDTO.builder()
								.idDTO(id)
								.emailDTO(email)
								.nameDTO(name)
								.surnameDTO(surname)
								.ageDTO(age)
								.nameSurnameDTO(nameSurname)
								.build();
		try {
			request.userDTOvalidation(request, validator);
			List<UserDTO> responseList=userService.getUsersByDTOFields(request);
			return new ResponseEntity<List<UserDTO>> ( responseList, HttpStatus.OK);

		}catch(NoSuchElementException nsee) {
			nsee.printStackTrace();
			return new ResponseEntity<List<UserDTO>> ( new ArrayList<>() , HttpStatus.NOT_FOUND);

		}catch(RuntimeException re) {
			re.printStackTrace();
			return new ResponseEntity<List<UserDTO>> ( new ArrayList<>(), HttpStatus.BAD_REQUEST);

		}
		
	}
	
	
	
	@GetMapping("/getAll") 
	public ResponseEntity<List<UserDTO>> getAll(){
		try {
			return ResponseEntity.ok(userService.getAllUsers());
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<List<UserDTO>>(new ArrayList<UserDTO>(), HttpStatus.NO_CONTENT	);
		}
	}
	
//	UPDATE
	
	
	@PutMapping("/update")
	public ResponseEntity<Long> update(@Validated @RequestBody UserDTO userDTO){
		try {
			return new ResponseEntity<Long>(userService.updateUserRecordByDto(userDTO, userDTO.getEmailDTO(), SearchFilterTypeEnum.EMAIL)
											,HttpStatus.OK);
		}catch(Exception e) {

			return new ResponseEntity<Long>(-1l,HttpStatus.NO_CONTENT);
		}
	}
	
	
	
//	DELETE
	
	@DeleteMapping("/delete")
	public ResponseEntity<Long> delete(@RequestParam  Long id) {
		try {
			Long idResponse= userService.deleteUserById(id);
			return new ResponseEntity<Long>(idResponse, HttpStatus.OK);

		}catch(RuntimeException re) {
			return new ResponseEntity<Long>( -1l, HttpStatus.NOT_FOUND);

		}
		
		
	}
	
	@DeleteMapping("/deleteAll")
	public ResponseEntity<List<UserDTO>> deleteAll(){
		try {
			userService.getAllUsers()
							.stream()
							.forEach(user->userService.deleteUserById(user.getIdDTO()));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return ResponseEntity.ok(userService.getAllUsers());
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<List<UserDTO>>(	new ArrayList<UserDTO>(),
									  	HttpStatus.NO_CONTENT	);
		}
	}

}
