package com.giogio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
			
		UserDTO userDTOFromDB=userServiceImpl.getSingleUserByFilter(userDTO.getEmailDTO(), searchFilterType);
		
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
				()->userServiceImpl.getSingleUserByFilter(userDTO.getEmailDTO(), searchFilterType)
				);
		
		verify(findByFilterSelection,times(1)).getOptionalUserEntity(userDTO.getEmailDTO(), searchFilterType);
		
		assertEquals(noSuchElementException.getMessage(),"User not found");
		
	}
	
	@Test
	void getUserByFilter_nullArguments() {
		
		final SearchFilterType searchFilterType=SearchFilterType.EMAIL;
		
		final String filter=null;
		
		IllegalArgumentException iae=assertThrows(IllegalArgumentException.class,
				()->userServiceImpl.getSingleUserByFilter(filter, searchFilterType));
		
		assertEquals(iae.getMessage(),"at least one argument is null");
		
		
		final SearchFilterType searchFilterType01=null;
		
		final String filter01="newEmail01@email.com";
		
		iae=assertThrows(IllegalArgumentException.class,
				()->userServiceImpl.getSingleUserByFilter(filter01, searchFilterType01));
		
		assertEquals(iae.getMessage(),"at least one argument is null");

		
		

	}
	
	
	
	@Test
	void getAllUsers(){
		
		List<UserEntity> userEntityList=
				IntStream.range(1, 1001)
						 .mapToObj(i->
						 	new UserEntity(Integer.toUnsignedLong(i),
									"name"+i,
									"surname"+i,
									(int)(Math.random()*60),
									"email"+i+"@email.it"))
						 .collect(Collectors.toList());
		
		List<UserDTO> userDTOListResponse=
				userEntityList.stream()
									.map(user-> fromUserEntityToUserDTO.doMapping(user))
									.toList();
				
		when(userRepository.findAll()).thenReturn(userEntityList);
		
		List<UserDTO> userDTOListResponseFromDB;
		try {
			userDTOListResponseFromDB = userServiceImpl.getAllUsers();
			assertEquals(userDTOListResponse,userDTOListResponseFromDB);

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	void updateUserRecordByDto() {
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(44)
				.emailDTO("Email01@email.com")
				.build();

		final UserEntity inMemoryUser=UserEntity.builder()
				.surname("OldSurname01")
				.name("OldName01")
				.age(33)
				.email("Email01@email.com")
				.id(3l)
				.build();
		
		final UserEntity inMemoryUserUpdated=UserEntity.builder()
				.surname("NewSurname01")
				.name("NewName01")
				.age(44)
				.email("Email01@email.com")
				.id(3l)
				.build();
	
		
		final String filter="Email01@email.com";
		
		when(findByFilterSelection.getOptionalUserEntity(filter,SearchFilterType.EMAIL )).thenReturn(Optional.of(inMemoryUser));
		
		when(userRepository.save(inMemoryUserUpdated)).thenReturn(inMemoryUserUpdated);
				
		Long userUpdatedId=userServiceImpl.updateUserRecordByDto(userDTO, filter, SearchFilterType.EMAIL);
				
		when(findByFilterSelection.getOptionalUserEntity(userUpdatedId,SearchFilterType.ID)).thenReturn(Optional.of(inMemoryUserUpdated));

		when(fromUserEntityToUserDTO.doMapping(inMemoryUserUpdated)).thenReturn(userDTO);
		
		UserDTO userRetrived=userServiceImpl.getSingleUserByFilter(userUpdatedId, SearchFilterType.ID);
		
		assertEquals(userRetrived.getSurnameDTO(),userDTO.getSurnameDTO());
		assertEquals(userRetrived.getNameDTO(),userDTO.getNameDTO());
		assertEquals(userRetrived.getAgeDTO(),userDTO.getAgeDTO());
		assertEquals(userRetrived.getEmailDTO(),userDTO.getEmailDTO());

	
		
	}
	@Test
	void updateUserRecordByDto_anyNullValues() {
		
		final UserDTO userDTORequest=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO(null)
				.ageDTO(null)
				.emailDTO("Email01@email.com")
				.build();
		
		final UserDTO userDTOResponse=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("OldName01")
				.ageDTO(33)
				.emailDTO("Email01@email.com")
				.build();

		final UserEntity inMemoryUser=UserEntity.builder()
				.surname("OldSurname01")
				.name("OldName01")
				.age(33)
				.email("Email01@email.com")
				.id(3l)
				.build();
		
		final UserEntity inMemoryUserUpdated=UserEntity.builder()
				.surname("NewSurname01")
				.name("OldName01")
				.age(33)
				.email("Email01@email.com")
				.id(3l)
				.build();
	
		
		final String emailFilter="Email01@email.com";
		
		when(findByFilterSelection.getOptionalUserEntity(emailFilter,SearchFilterType.EMAIL )).thenReturn(Optional.of(inMemoryUser));
		
		when(userRepository.save(inMemoryUserUpdated)).thenReturn(inMemoryUserUpdated);
				
		Long userUpdatedId=userServiceImpl.updateUserRecordByDto(userDTORequest, emailFilter, SearchFilterType.EMAIL);
				
		when(findByFilterSelection.getOptionalUserEntity(userUpdatedId,SearchFilterType.ID)).thenReturn(Optional.of(inMemoryUserUpdated));

		when(fromUserEntityToUserDTO.doMapping(inMemoryUserUpdated)).thenReturn(userDTOResponse);
		
		UserDTO userRetrived=userServiceImpl.getSingleUserByFilter(userUpdatedId, SearchFilterType.ID);
		
		verify(findByFilterSelection,times(1)).getOptionalUserEntity(emailFilter,SearchFilterType.EMAIL );
		verify(findByFilterSelection,times(1)).getOptionalUserEntity(userUpdatedId,SearchFilterType.ID );
		verify(userRepository,times(1)).save(inMemoryUserUpdated);
		verify(fromUserEntityToUserDTO,times(1)).doMapping(inMemoryUserUpdated);

		assertNotEquals(userUpdatedId,-1l);
		
		assertEquals(userRetrived.getSurnameDTO(),userDTOResponse.getSurnameDTO());
		assertEquals(userRetrived.getNameDTO(),userDTOResponse.getNameDTO());
		assertEquals(userRetrived.getAgeDTO(),userDTOResponse.getAgeDTO());
		assertEquals(userRetrived.getEmailDTO(),userDTOResponse.getEmailDTO());

	}
	
	@Test
	void updateUserRecordByDto_userNotFound() {
		
		final UserDTO userDTO=UserDTO.builder()
				.surnameDTO("NewSurname01")
				.nameDTO("NewName01")
				.ageDTO(44)
				.emailDTO("Email01@email.com")
				.build();
		
		final String filter="Email01@email.com";
		
		when(findByFilterSelection.getOptionalUserEntity(filter,SearchFilterType.EMAIL )).thenReturn(Optional.empty());
						
		Long userUpdatedId=userServiceImpl.updateUserRecordByDto(userDTO, filter, SearchFilterType.EMAIL);
						
		assertEquals(userUpdatedId,-1L);
		
		
	}
	
	


}
