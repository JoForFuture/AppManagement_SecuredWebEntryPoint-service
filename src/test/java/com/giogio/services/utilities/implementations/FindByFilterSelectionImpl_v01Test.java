package com.giogio.services.utilities.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;
import com.giogio.services.utilities.SearchFilterType;

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
							.id(4l)
							.build()
				);
		
		
		final String emailFilter="user01@email.com";
		
		final SearchFilterType searchFilterTypeEMAIL=SearchFilterType.EMAIL;
		
		when(userRepository.findUserEntityByEmail(emailFilter)).thenReturn(user);
				
		Optional<UserEntity> userFromDBByEmail=findByFilterSelectionImpl_v01.getOptionalUserEntity(emailFilter, searchFilterTypeEMAIL);
		
		assertEquals(userFromDBByEmail.get().getEmail(), emailFilter);
		
		
		final Long idFilter=4l;
	
		final SearchFilterType searchFilterTypeID=SearchFilterType.ID;
	
		when(userRepository.findUserEntityById(idFilter)).thenReturn(user);
			
		Optional<UserEntity> userFromDBByID=findByFilterSelectionImpl_v01.getOptionalUserEntity(idFilter, searchFilterTypeID);
		
		assertEquals(userFromDBByID.get().getId(), idFilter);


		

		
		
	}

}
