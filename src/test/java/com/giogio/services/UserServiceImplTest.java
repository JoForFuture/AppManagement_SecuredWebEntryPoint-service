package com.giogio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

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
	void getUserByFilter_UserFound() {
		// da completare				
		final SearchFilterType searchFilterType=SearchFilterType.EMAIL;
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.emailDTO("newEmail01@email.com")
				.build();
		
		final UserEntity inMemoryUser=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(33)
				.email("newEmail01@email.com")
				.id(3l)
				.build();
		
		

		
		when(findByFilterSelection.getOptionalUserEntity(userDTO.getEmailDTO(),searchFilterType)).thenReturn(Optional.of(inMemoryUser));
		when(fromUserEntityToUserDTO.doMapping(inMemoryUser)).thenReturn(userDTO);
			
		UserDTO userDTOFromDB=userServiceImpl.getUserByFilter(userDTO.getEmailDTO(), searchFilterType);
		
		verify(findByFilterSelection,times(1)).getOptionalUserEntity(userDTO.getEmailDTO(), searchFilterType);
		verify(fromUserEntityToUserDTO,times(1)).doMapping(inMemoryUser);
		
		assertEquals(userDTOFromDB.getSurnameDTO(),inMemoryUser.getSurname());
		assertEquals(userDTOFromDB.getNameDTO(),inMemoryUser.getName());
		assertEquals(userDTOFromDB.getAgeDTO(),inMemoryUser.getAge());
		assertEquals(userDTOFromDB.getEmailDTO(),inMemoryUser.getEmail());

	}
	
	@Test
	void getUserByFilter_UserNotFound() {
		final SearchFilterType searchFilterType=SearchFilterType.EMAIL;
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(33)
				.emailDTO("newEmail01@email.com")
				.build();
		
		when(findByFilterSelection.getOptionalUserEntity(userDTO.getEmailDTO(),searchFilterType)).thenReturn(Optional.empty());
		
		NoSuchElementException noSuchElementException=assertThrows(
				NoSuchElementException.class,
				()->userServiceImpl.getUserByFilter(userDTO.getEmailDTO(), searchFilterType)
				);
		
		verify(findByFilterSelection,times(1)).getOptionalUserEntity(userDTO.getEmailDTO(), searchFilterType);
		
		assertEquals(noSuchElementException.getMessage(),"User not found");
		
	}
	
	@Test
	void getAllUsers() throws NotFoundException {
		
		List<UserEntity> userEntityListFunctional=
				IntStream.range(1, 1001)
						 .mapToObj(i->
						 	new UserEntity(Integer.toUnsignedLong(i),
									"name"+i,
									"surname"+i,
									(int)(Math.random()*60),
									"email"+i+"@email.it"))
						 .collect(Collectors.toList());
		
		List<UserDTO> userDTOListResponse=
				userEntityListFunctional.stream()
									.map(user-> fromUserEntityToUserDTO.doMapping(user))
									.toList();
				
		when(userRepository.findAll()).thenReturn(userEntityListFunctional);
		
		List<UserDTO> userDTOListResponseFromDB=userServiceImpl.getAllUsers();
		
		assertEquals(userDTOListResponse,userDTOListResponseFromDB);
		
		

		
		
		
	}


}
