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
	public void doMapping_with_not_null_values(){
		UserDTO userDTO=UserDTO.builder()
								.surnameDTO("Surname01")
								.nameDTO("")
								.ageDTO(20)
								.build();
		String email="First@email.com";
		
		UserEntity userEntity=fromUserDTOToUserEntity_v02.doMapping(userDTO, email);
		
		assertEquals(userEntity.getSurname(),userDTO.getSurnameDTO());
		assertEquals(userEntity.getName(),userDTO.getNameDTO());
		assertEquals(userEntity.getAge(),userDTO.getAgeDTO());
		assertEquals(userEntity.getEmail(),email);
	
	}
	
	@Test
	public void doMapping_with_null_fields(){
		UserDTO userDTO=UserDTO.builder()
								.build();
		String email=null;
		
		UserEntity userEntity=fromUserDTOToUserEntity_v02.doMapping(userDTO, email);
		
		assertEquals(userEntity.getSurname(),"");
		assertEquals(userEntity.getName(),"");
		assertEquals(userEntity.getAge(),0);
		assertEquals(userEntity.getEmail(),"");
	
	}
	
	@Test
	public void doMapping_with_null_userDTO(){
		UserDTO userDTO=null;
		String email=null;
				
		NullPointerException myNullPointerException=assertThrows(
																	NullPointerException.class,
																	()->fromUserDTOToUserEntity_v02.doMapping(userDTO, email)
																	);
		
		assertEquals(myNullPointerException.getMessage(),"userDTO passed as argument is null");
	}
	

}
