package com.giogio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.giogio.services.utilities.FromUserEntityToUserDTO;
import com.giogio.services.utilities.NotificationSender;
import com.giogio.services.utilities.SearchFilterType;

@SpringBootTest
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	@Mock
	private FromUserDTOToUserEntity fromUserDTOToUserEntity;
	@Mock
	private FromUserEntityToUserDTO fromUserEntityToUserDTO;
	@Mock
	private NotificationSender notificationSender;
	@Mock
	private FindByFilterSelection findByFilterSelection;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	void addUserIfNotPresentTest_effectiveNewUser() {
		
		final UserDTO userDTO=UserDTO.builder()
								.surnameDTO("NewSurname01")
								.nameDTO("NewName01")
								.ageDTO(33)
								.emailDTO("newEmail01@email.com")
								.build();
				
		final UserEntity userToSave=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email("newEmail01@email.com")
				.build();
		
		final UserEntity savedUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email("newEmail01@email.com")
				.id(3l)
				.build();
		
		
		 Optional<UserEntity> notFoundUser=Optional.empty();
		
		when(userRepository.findUserEntityByEmail(userDTO.getEmailDTO())).thenReturn(notFoundUser);
		
		when(userRepository.save(userToSave)).thenReturn(savedUser);
		
		when(fromUserDTOToUserEntity.doMapping(userDTO)).thenReturn(userToSave);
		
		Long idSavedUser=userServiceImpl.addUserIfNotPresent(userDTO);
		
		verify(notificationSender,times(1)).notifyMessage("user with name: \" "+ savedUser +" \" saved");
		
		verify(userRepository,times(1)).findUserEntityByEmail(userDTO.getEmailDTO());
		
		verify(userRepository,times(1)).save(userToSave);
		
		verify(fromUserDTOToUserEntity,times(1)).doMapping(userDTO);

		assertEquals(userToSave.getSurname(),savedUser.getSurname());
		
		assertEquals(userToSave.getName(),savedUser.getName());
		
		assertEquals(userToSave.getAge(),savedUser.getAge());
		
		assertEquals(userToSave.getEmail(),savedUser.getEmail());


		assertEquals(idSavedUser,savedUser.getId());
		
		
	}
	
	@Test
	void addUserIfNotPresentTest_whenUserAlreadyExist() {
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.emailDTO("newEmail01@email.com")
				.build();

		final UserEntity savedUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email("newEmail01@email.com")
				.id(3l)
				.build();
		
		Optional<UserEntity> foundUser=Optional.of(savedUser);
			
		when(userRepository.findUserEntityByEmail(userDTO.getEmailDTO())).thenReturn(foundUser);
			
		Long idSavedUser=userServiceImpl.addUserIfNotPresent(userDTO);
			
		verify(notificationSender,times(1)).notifyMessage("user with name: \" "+savedUser.getName()+" \" already exist");
			
		verify(userRepository,times(1)).findUserEntityByEmail(userDTO.getEmailDTO());
			
		assertEquals(idSavedUser,-1l);
	}
	
	@Test
	void addUserIfNotPresentTest_atLeastOneArgumentIsNull() {
		
		final UserDTO userDTONull=null;
		
		final IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()-> userServiceImpl.addUserIfNotPresent(userDTONull));
				
		assertEquals(exception.getMessage(),"at least one argument is null");
	}
	
	
	@Test
	void getUserByFilter() {
		// da completare
		final String filter="newEmail01@email.com";
				
		final SearchFilterType searchFilterType=SearchFilterType.EMAIL;
		
		final UserEntity inMemoryUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email("newEmail01@email.com")
				.id(3l)
				.build();
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.emailDTO("newEmail01@email.com")
				.build();

		
		when(findByFilterSelection.getOptionalUserEntity(filter,searchFilterType)).thenReturn(Optional.of(inMemoryUser));
		
		when(fromUserEntityToUserDTO.doMapping(inMemoryUser)).thenReturn(userDTO);
		
		
		
	}

}
