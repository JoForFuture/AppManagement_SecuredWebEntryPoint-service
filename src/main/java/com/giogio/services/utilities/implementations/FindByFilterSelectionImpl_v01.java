package com.giogio.services.utilities.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;
import com.giogio.services.utilities.SearchFilterType;

@Primary
@Qualifier("FindByFilterSelectionImpl_v01")
@Component
public class FindByFilterSelectionImpl_v01 implements FindByFilterSelection {
	
	private final UserRepository userRepository;
	
	
	public FindByFilterSelectionImpl_v01(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public Optional<UserEntity> getOptionalUserEntity(Object filter,SearchFilterType searchFilterType){
		Optional<UserEntity> user;
		
		
		switch(searchFilterType) {
		case EMAIL:
			user=userRepository.findUserEntityByEmail((String)filter);
			return user;
		case ID:
			user=userRepository.findUserEntityById((Long)filter);
			return user;
		default:
			throw new RuntimeException("wrong filter type");
		}
				
			
	}
}
