package com.giogio.services.utilities.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

@SpringBootTest
public class fromUserEntityToUserDTOImpl_v01Test {
	
	
	@InjectMocks
	FromUserEntityToUserDTOImpl_v01 fromUserEntityToUserDTOImpl_v01;
	
	@Test
	void doMapping(){
		
		UserEntity userEntity=UserEntity.builder()
										.surname("Surname01")
										.name("name01")
										.age(23)
										.email("email01@email.com")
										.build();
		
		UserDTO userDTO=fromUserEntityToUserDTOImpl_v01.doMapping(userEntity);
		
		assertEquals(userDTO.getSurnameDTO(),userEntity.getSurname());
		assertEquals(userDTO.getNameDTO(),userEntity.getName());
		assertEquals(userDTO.getAgeDTO(),userEntity.getAge());
		assertEquals(userDTO.getEmailDTO(),userEntity.getEmail());
	
	}
	
	@Test
	void doMapping_nullArgument(){
		
		UserEntity userEntity=null;
		
		IllegalArgumentException iae=assertThrows(IllegalArgumentException.class,()->fromUserEntityToUserDTOImpl_v01.doMapping(userEntity));

		assertEquals(iae.getMessage(),"userEntity is null");
	}
	
	

}
