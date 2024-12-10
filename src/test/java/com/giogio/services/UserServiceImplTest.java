package com.giogio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;
import com.giogio.services.utilities.FromUserDTOToUserEntity;
import com.giogio.services.utilities.NotificationSender;

@SpringBootTest
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	@Mock
	private FromUserDTOToUserEntity fromUserDTOToUserEntity;
	@Mock
	private NotificationSender notificationSender;
	@Mock
	private FindByFilterSelection findByFilterSelection;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	void addUserIfNotPresentTest() {
		
		final UserDTO userDTO=UserDTO.builder()
								.surnameDTO("NewSurname01")
								.nameDTO("NewName01")
								.ageDTO(33)
								.build();
		
		final String email="newEmail01@email.com";
		
		final UserEntity userToSave=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email(email)
				.build();
		
		final UserEntity savedUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email(email)
				.id(3l)
				.build();
		
		
		 Optional<UserEntity> notFoundUser=Optional.empty();
		
		when(userRepository.findUserEntityByEmail(email)).thenReturn(notFoundUser);
		
		when(userRepository.save(userToSave)).thenReturn(savedUser);
		
		when(fromUserDTOToUserEntity.doMapping(userDTO, email)).thenReturn(userToSave);
		
		Long idSavedUser=userServiceImpl.addUserIfNotPresent(userDTO, email);
		
		verify(notificationSender,times(1)).notifyMessage("user with name: \" "+ savedUser +" \" saved");
		
		verify(userRepository,times(1)).findUserEntityByEmail(email);
		
		verify(userRepository,times(1)).save(userToSave);
		
		verify(fromUserDTOToUserEntity,times(1)).doMapping(userDTO, email);

		
		assertEquals(idSavedUser,savedUser.getId());
		
		
	}
	
	@Test
	void addUserIfNotPresentTest_whenUserAlreadyExist() {
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.build();

		final String email="newEmail01@email.com";

		final UserEntity savedUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email(email)
				.id(3l)
				.build();
		
		Optional<UserEntity> foundUser=Optional.of(savedUser);
			
		when(userRepository.findUserEntityByEmail(email)).thenReturn(foundUser);
			
		Long idSavedUser=userServiceImpl.addUserIfNotPresent(userDTO, email);
			
		verify(notificationSender,times(1)).notifyMessage("user with name: \" "+savedUser.getName()+" \" already exist");
			
		verify(userRepository,times(1)).findUserEntityByEmail(email);
			
		assertEquals(idSavedUser,-1l);
	}
	
	@Test
	void addUserIfNotPresentTest_atLeastOneArgumentIsNull() {
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.build();

		final String emailNull=null;
		
		final IllegalArgumentException exception01=assertThrows(IllegalArgumentException.class,()-> userServiceImpl.addUserIfNotPresent(userDTO, emailNull));
		
		assertEquals(exception01.getMessage(),"at least one argument is null");
		
		final UserDTO userDTONull=null;

		final String email="newEmail01@email.com";
		
		final IllegalArgumentException exception02=assertThrows(IllegalArgumentException.class,()-> userServiceImpl.addUserIfNotPresent(userDTONull, email));
				
		assertEquals(exception02.getMessage(),"at least one argument is null");
	}

}
