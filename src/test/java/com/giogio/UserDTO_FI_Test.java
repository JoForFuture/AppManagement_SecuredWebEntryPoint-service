package com.giogio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.UserDTO_FI;

@SpringBootTest
public class UserDTO_FI_Test {

	@Test
	void updateUserEntityFrom_NameNotNullAndNotEmpty_Test() {
		 String identifier="###################";
		 String userEntityOldName="userEntityOldName";

		 UserDTO userDTO=UserDTO.builder()
				 						 .nameDTO("userDTOname")
				 						 .build();
		 
		 UserEntity userEntity=UserEntity.builder()
		 								 .name("userEntityOldName")
		 								 .build();
		 
		 System.out.println(identifier+userEntity.getName()) ;
		 assertEquals(userEntity.getName(),userEntityOldName);
		 
		 userDTO.nameNotNullAndNotEmpty(userDTO.getNameDTO(), s->{
			userEntity.setName(s);
			return  userDTO;
		 });
		 
		 System.out.println(identifier+userEntity.getName()) ;
		 assertEquals(userEntity.getName(),userDTO.getNameDTO());
		 
	 }
	
	
	@Test
	void updateUserEntityFrom_NullOrEmpty_Test() {
		 String identifier="*****************";
		 String userEntityOldName="userEntityOldName";

		 UserDTO userDTO=UserDTO
				 				.builder()
				 				.build();
		 
		 UserEntity userEntity=UserEntity.builder()
		 								 .name("userEntityOldName")
		 								 .build();
		 
		 System.out.println(identifier+userEntity.getName()) ;
		 assertEquals(userEntity.getName(),userEntityOldName);
		 
		 userDTO.nameNotNullAndNotEmpty(userDTO.getNameDTO(), s->{
			userEntity.setName(s);
			return  userDTO;
		 });
		 
		 System.out.println(identifier+userEntity.getName()) ;
		 assertEquals(userEntity.getName(),userEntityOldName);
		 
	 }
	

}
