package com.giogio.services.utilities.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;

@SpringBootTest
public class FindByFilterSelectionImpl_v01Test {
	
	@Mock
	static UserRepository userRepository;
	
	@InjectMocks
	FindByFilterSelectionImpl_v01 findByFilterSelectionImpl_v01;

	
	@Test
	public void getOptionalUserEntityTest() {
		
		final Optional<UserEntity> user=Optional.of(
				UserEntity.builder()
							.surname("surname01")
							.name("name01")
							.age(01)
							.email("user01@email.com")
							.build()
				);
		
		final String filter="user01@email.com";
		
		Mockito.when(userRepository.findUserEntityByEmail((String)filter)).thenReturn(user);
		
		Optional<UserEntity> userFromDB=findByFilterSelectionImpl_v01.getOptionalUserEntity(filter);
		
		assertEquals(userFromDB.get().getEmail(), filter);
		
		
	}

}
