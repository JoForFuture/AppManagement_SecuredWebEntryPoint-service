package com.giogio.services.utilities.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

@SpringBootTest
public class FromUserDTOToUserEntity_v02Test {
	
	static FromUserDTOToUserEntity_v02 fromUserDTOToUserEntity_v02;
	
	@BeforeAll
	public static void setup() {
		fromUserDTOToUserEntity_v02 = new FromUserDTOToUserEntity_v02();
	}
	
	@Test
	public void doMapping_withNotNullValues(){
		UserDTO userDTO=UserDTO.builder()
								.surnameDTO("Surname01")
								.nameDTO("")
								.ageDTO(20)
								.emailDTO("First@email.com")
								.build();
		
		UserEntity userEntity=fromUserDTOToUserEntity_v02.doMapping(userDTO);
		
		assertEquals(userEntity.getSurname(),userDTO.getSurnameDTO());
		assertEquals(userEntity.getName(),userDTO.getNameDTO());
		assertEquals(userEntity.getAge(),userDTO.getAgeDTO());
		assertEquals(userEntity.getEmail(),userDTO.getEmailDTO());
	
	}
	
	@Test
	public void doMapping_withNullFields(){
		UserDTO userDTO=UserDTO.builder()
								.build();
		
		UserEntity userEntity=fromUserDTOToUserEntity_v02.doMapping(userDTO);
		
		assertEquals(userEntity.getSurname(),"");
		assertEquals(userEntity.getName(),"");
		assertEquals(userEntity.getAge(),0);
		assertEquals(userEntity.getEmail(),"");
	
	}
	
	@Test
	public void doMapping_withNullUserDTO(){
		final UserDTO userDTONull=null;
				
		IllegalArgumentException myNullPointerException01=assertThrows(
																	IllegalArgumentException.class,
																	()->fromUserDTOToUserEntity_v02.doMapping(userDTONull)
																	);
		
		assertEquals(myNullPointerException01.getMessage(),"at least one argument is null");
	

	}
	

}
